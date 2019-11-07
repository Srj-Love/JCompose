package com.example.jcompose

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import android.widget.Space
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.*
import androidx.lifecycle.lifecycleScope
import androidx.ui.core.*
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.DrawShape
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Image
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.graphics.vector.compat.vectorResource
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Card
import androidx.ui.text.TextStyle
import com.example.jcompose.http.BookHttp
import com.example.jcompose.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookActivity : AppCompatActivity() {

    private val booksResult: ListBookResult =
        ListBookResult(false, emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContent {
            MaterialTheme {
                BooksScreen(booksResult)
            }
        }

        lifecycleScope.launch {
            booksResult.loading = true
            val books = withContext(Dispatchers.IO) {
                BookHttp.loadBooksGson()
            }
            booksResult.books = books ?: emptyList()
            booksResult.loading = false
        }
    }


    @Composable
    fun DeleteFavBookDialog(
        resources: Resources,
        book: Book,
        onConfirm: (Book) -> Unit,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onCloseRequest = onDismiss,
            title = {
                Text(
                    text = "Delete",
                    style = +themeTextStyle { h6 }
                )
            },
            text = {
                Text(
                    text = resources.getString(
                        R.string.msg_fav_delete_message, book.title
                    ),
                    style = +themeTextStyle { body2 }
                )
            },
            confirmButton = {
                Button(
                    text = resources.getString(
                        R.string.msg_fav_delete_confirm
                    ),
                    style = ContainedButtonStyle(),
                    onClick = {
                        onConfirm(book)
                        onDismiss()
                    }
                )
            },
            dismissButton = {
                Button(
                    text = resources.getString(
                        R.string.msg_fav_delete_cancel
                    ),
                    style = TextButtonStyle(),
                    onClick = onDismiss
                )
            }
        )
    }

    @Model
    data class BookScreenState(
        var selectedTab: Int,
        var isDeleteDialogOpen: Boolean,
        var bookToDelete: Book?,
        var booksFavorites: MutableSet<Book> = mutableSetOf()
    )

    @Composable
    fun BooksScreen(result: ListBookResult) {
        val context = +ambient(ContextAmbient)
        val resources = context.resources

        if (result.loading) {
            Loading(resources)
            return
        }
        val screenState by +state {
            BookScreenState(0, false, null)
        }

        if (screenState.isDeleteDialogOpen) {
            screenState.bookToDelete?.let { book ->
                DeleteFavBookDialog(
                    resources,
                    book,
                    onConfirm = { bookToDelete ->
                        screenState.booksFavorites.remove(bookToDelete)
                    },
                    onDismiss = {
                        screenState.run {
                            bookToDelete = null
                            isDeleteDialogOpen = false
                        }
                    }
                )
            }
        }
        BooksScreenContent(context, resources, result, screenState)
    }

    @Model
    data class ListBookResult(
        var loading: Boolean,
        var books: List<Book>
    )

    @Composable
    fun Loading(resources: Resources) {
        Container(
            alignment =
            Alignment.Center, expanded = true
        ) {
            Text("Loading books",
                style = +themeTextStyle { h6 }
            )
        }
    }

    @Composable
    fun Tabs(
        resources: Resources,
        selectedTab: Int,
        onSelected: (Int) -> Unit
    ) {
        TabRow(
            items = listOf(
                resources.getString(R.string.tab_books),
                resources.getString(R.string.tab_favorites)
            ),
            selectedIndex = selectedTab,
            tab = { index, string ->
                Tab(
                    text = string,
                    selected = selectedTab == index,
                    onSelected = {
                        onSelected(index)
                    }
                )
            }
        )
    }

    @Composable
    fun BooksList(
        resources: Resources,
        books: Collection<Book>?,
        Action: (Book) -> Unit
    ) {
        if (books == null || books.isEmpty()) {
            Container(expanded = true, alignment = Alignment.Center) {
                Text(
                    resources.getString(R.string.msg_book_list_empty),
                    style = +themeTextStyle { h6 }
                )
            }
            return
        }
        Container(expanded = true, alignment = Alignment.TopLeft) {
            VerticalScroller {
                Column {
                    books.forEach { book ->
                        BookItem(resources, book, Action)
                    }
                }
            }
        }
    }

    @Composable
    fun BookItem(
        resources: Resources,
        book: Book,
        action: (Book) -> Unit
    ) {
        Container(
            alignment =  Alignment.Center, expanded = true) {
            Card(shape = RoundedCornerShape(4.dp)) {
                Ripple(bounded = true) {
                    Clickable(onClick = {
                        action(book)
                    }) {
                        BookItemContent(resources, book)
                    }
                }
            }
        }
    }

    @Composable
    fun BookItemContent(
        resources: Resources,
        book: Book
    ) {
        Row(mainAxisSize = LayoutSize.Expand) {
            com.example.bookscompose.Image(url = book.coverUrl, width = 96.dp, height = 144.dp)
            Column(
                /*modifier = Space(16.dp)
                        mainAxisSize = LayoutSize . Expand,*/
                crossAxisSize = LayoutSize.Expand
            ) {
                Text(
                    text = book.title,
                    style = (+themeTextStyle { h6 })
                        .withOpacity(0.87f)
                )
                Text(
                    text = book.author,
                    style = (+themeTextStyle { body2 })
                        .withOpacity(0.87f)
                )
                Text(
                    text = resources.getString(R.string.book_info_year_pages).plus(book.year).plus(book. pages),
                    style = (+themeTextStyle { body2 })
                        .withOpacity(0.6f)
                )

            }
        }
    }


    @Composable
    fun BooksScreenContent(
        context: Context,
        resources: Resources,
        result: ListBookResult,
        screenState: BookScreenState
    ) {
        FlexColumn {
            DrawShape(shape = RectangleShape, color = Color(0xfafafa))
            inflexible {
                Tabs(resources = resources,
                    selectedTab = screenState.selectedTab,
                    onSelected = { index ->
                        screenState.selectedTab = index
                    })
            }
            expanded(1f) {
                when (screenState.selectedTab) {
                    0 -> BooksList(resources, result.books) { book ->
                        screenState.booksFavorites.add(book)
                        Toast.makeText(
                            context,
                            resources.getString(
                                R.string.msg_added_favorites,
                                book.title
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    1 -> BooksList(
                        resources,
                        screenState.booksFavorites
                    ) { book ->
                        screenState.run {
                            bookToDelete = book
                            isDeleteDialogOpen = true
                        }
                    }
                }
            }
        }
        BottomRightFab()
    }


    @Composable
    fun BottomRightFab() {
        Container(
            expanded = true,
            alignment = Alignment.BottomRight,
            padding = EdgeInsets(all = 16.dp)
        ) {
            FloatingActionButton(
                color = Color.Red,
                onClick = {
                    // TODO
                }
            ) {
                Container(width = 24.dp, height = 24.dp) {
                    DrawVector(
                        +vectorResource(R.drawable.ic_baseline_add)
                    )
                }
            }
        }
    }

}

