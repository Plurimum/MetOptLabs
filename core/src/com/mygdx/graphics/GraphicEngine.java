package com.mygdx.graphics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.methods.BrentCombMethod;
import com.mygdx.nmethods.AbstractNMethod;
import com.mygdx.nmethods.GradientMethod;
import com.mygdx.nmethods.GradientOpt;
import com.mygdx.parser.ExpressionAlgebra;
import com.mygdx.parser.ExpressionParser;
import com.mygdx.parser.expression.Expression;

import java.util.function.Function;

public class GraphicEngine extends ApplicationAdapter {
    private Batch batch;
    private ShapeRenderer graphicRenderer;
    private Stage stage;
    private Graphic graphic;
    private RenderFunction func;
    private Input.TextInputListener inputFunction;
    private Input.TextInputListener inputEps;
    private ExpressionParser<Expression> parser;
    private double eps;


    private void inputFunction(final String title) {
        Gdx.input.getTextInput(inputFunction, title, "10*x*x - x*y + y*y - 2*x + y + 8", "");
    }

    private void inputEps(final String title) {
        Gdx.input.getTextInput(inputEps, title, "1e-5", "");
    }

    private void callTest(RenderFunction func, Function<RenderFunction, AbstractNMethod<RenderFunction>> methodCreator) {
        new DrawableNMethod(methodCreator.apply(func)).findMin(eps);
        graphic.setMain(func);
    }

    private Image addButton(final String name, final float x, final float y, Runnable runnable) {
        Image b = new Image(new Texture("button_" + name + ".png"));
        b.setPosition(x, y);
        stage.addActor(b);
        b.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runnable.run();
            }
        });
        return b;
    }

    private void addButtonMethod(
            final String name,
            final float x,
            final float y,
            final Function<RenderFunction, AbstractNMethod<RenderFunction>> methodCreator) {
        Image b = addButton(name, x, y, () -> callTest(func, methodCreator));
        stage.addActor(b);
    }

    private void initInputs() {
        inputFunction = new Input.TextInputListener() {
            @Override
            public void input(final String text) {
                func = new RenderFunction(parser.parse(text));
                inputEps("Input epsilon");
            }

            @Override
            public void canceled() {
                if (func == null) {
                    inputFunction("эй");
                }
            }
        };

        inputEps = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                eps = Double.parseDouble(text);
            }

            @Override
            public void canceled() {
                inputEps("эй");
            }
        };
    }

    @Override
    public void create() {
        parser = new ExpressionParser<>(new ExpressionAlgebra());
        stage = new Stage();
        batch = stage.getBatch();
        initInputs();
        inputFunction("Enter function, use +-*");
        graphicRenderer = new ShapeRenderer();
        graphic = new Graphic(graphicRenderer, stage);
        graphic.setSize(900, 900);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(graphic);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        addButtonMethod("gradient-method", 1100, 800, GradientMethod::new);
        addButtonMethod("optimized-gradient", 1100, 650, func -> new GradientOpt<>(func, BrentCombMethod::new));
        // addButtonMethod("conjugate-gradient", 1100, 500, NonlinearConjugateGradientMethod::new);
        addButton("change-function", 1100, 350, () -> inputFunction("Input function"));
        addButton("level-lines-toggle", 1500, 350, () -> graphic.draw_levels = !graphic.draw_levels);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        graphicRenderer.begin(ShapeRenderer.ShapeType.Filled);
        graphicRenderer.setColor(Color.BLACK);
        stage.act();
        batch.end();
        graphicRenderer.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        graphicRenderer.dispose();
    }
}