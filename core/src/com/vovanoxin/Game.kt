package com.vovanoxin

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import java.sql.Time

private val WORLD_WIDTH = 800f
private val WORLD_HEIGHT = 480f

private val VIEWPORT_WIDTH = 224f
private val VIEWPORT_HEIGHT = 352f


class Game : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var img: Texture
    lateinit var camera: OrthographicCamera
    lateinit var viewport: FitViewport
    lateinit var shapeRenderer: ShapeRenderer
    lateinit var gameField: GameField



    override fun create() {
        batch = SpriteBatch()
        camera = OrthographicCamera()
        camera.setToOrtho(false, 800F, 480F)
        viewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera)
        shapeRenderer = ShapeRenderer()
        gameField = GameField()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun render() {
        val start = System.currentTimeMillis()

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        gameField.queryInput()
        gameField.update()

        shapeRenderer.projectionMatrix = camera.projection
        shapeRenderer.transformMatrix = camera.view
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)

        drawGrid(shapeRenderer, viewport, gameField)

        shapeRenderer.end()
        println()
        Thread.sleep(start - System.currentTimeMillis()+60)
    }

    override fun dispose() {
        batch.dispose()
        shapeRenderer.dispose()
    }
}
fun drawGrid(shapeRenderer: ShapeRenderer, viewport: FitViewport, gameField: GameField){
    val GRIDSIZE = 100
    var CELL_SIZE = listOf(viewport.worldHeight, viewport.worldWidth).min()?.div(GRIDSIZE)!!

    var horizontalShift = 0f
    var verticalShift = 0f

    if(viewport.worldWidth > viewport.worldHeight){
        horizontalShift = (viewport.worldWidth - viewport.worldHeight)/2
    }else if(viewport.worldHeight > viewport.worldWidth){
        verticalShift = (viewport.worldHeight - viewport.worldWidth)/2
    }


    var x = 0
    while(x < GRIDSIZE){
        var y = 0
        while (y < GRIDSIZE){

            when(gameField.grid[x][y].type) {
                CellType.Body -> shapeRenderer.color = Color.GREEN
                CellType.Head -> shapeRenderer.color = Color.BROWN
                CellType.Food -> shapeRenderer.color = Color.RED
                else -> shapeRenderer.color = Color.WHITE
            }

            shapeRenderer.rect(x*CELL_SIZE+horizontalShift, y*CELL_SIZE+verticalShift, CELL_SIZE, CELL_SIZE)
            y++
        }
        x++
    }
}
