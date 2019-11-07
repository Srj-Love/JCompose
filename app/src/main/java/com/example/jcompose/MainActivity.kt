package com.example.jcompose

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.ui.core.*
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.text.TextStyle
import com.example.jcompose.model.Book

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContent { MyApp("Hello Srjlove") }
    }

    @Composable
    fun MyApp(title: String) {
        var counter = +state { 0 }
        MaterialTheme {
            FlexColumn {
                inflexible {
                    TopAppBar<MenuItem>(
                        title = {
                            Text(
                                "Welcome in ${getString(R.string.app_name)}",
                                style = TextStyle(color = Color.Cyan)
                            )
                        },
                        color = Color.Black
                    )
                }
                expanded(1F) {
                    Center {
                        Column(
                            mainAxisAlignment = MainAxisAlignment.Center,
                            crossAxisAlignment = CrossAxisAlignment.Center
                        ) {

                            Text(
                                "Counter will start here ${counter.value}"
                            )

                            Button(
                                text = "Book List",
                                style = ContainedButtonStyle(),
                                onClick = {
                                    startActivity(Intent(this@MainActivity,BookActivity::class.java ))
                                }
                            )
                        }
                    }
                }
                inflexible {
                    Row(
                        mainAxisAlignment = MainAxisAlignment.SpaceAround,
                        mainAxisSize = LayoutSize.Expand
                    ) {
                        Padding(padding = EdgeInsets(all = 16.dp)) {
                            FloatingActionButton(
                                text = "Add", onClick = {
                                    counter.value++
                                }, color = Color.Cyan, textStyle = TextStyle(
                                    color = Color.Black
                                )
                            )
                        }
                        Padding(padding = EdgeInsets(all = 16.dp)) {
                            FloatingActionButton(
                                text = "Substract", onClick = {
                                    counter.value--
                                }, color = Color.Cyan, textStyle = TextStyle(
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
                inflexible {
                    BottomAppBar<MenuItem>(
                        color = Color.Cyan
                    )
                }
            }
        }
    }

}

