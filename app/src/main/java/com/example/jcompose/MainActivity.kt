package com.example.jcompose

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.state
import androidx.compose.unaryPlus
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.core.setContent
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.BottomAppBar
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TopAppBar
import androidx.ui.text.TextStyle

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
                        color = Color.Black
                    )
                }
            }
        }
    }


}

