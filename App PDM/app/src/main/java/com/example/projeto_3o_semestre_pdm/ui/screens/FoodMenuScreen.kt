package com.example.projeto_3o_semestre_pdm.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.projeto_3o_semestre_pdm.R
import com.example.projeto_3o_semestre_pdm.data.model.MenuCategory
import com.example.projeto_3o_semestre_pdm.data.model.MenuItem
import com.example.projeto_3o_semestre_pdm.ui.navigation.Screen
import com.example.projeto_3o_semestre_pdm.ui.theme.AccentTeal
import com.example.projeto_3o_semestre_pdm.ui.theme.BackgroundLight
import com.example.projeto_3o_semestre_pdm.ui.theme.PrimaryBlue

data class CartItem(val menuItem: MenuItem, var quantity: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodMenuScreen(navController: NavController) {
    val menuItems = remember { getMenuItems() }
    var cartVersion by remember { mutableStateOf(0) } // Force recomposition
    val cart = remember { mutableStateMapOf<Int, CartItem>() }

    val totalItems = cart.values.sumOf { it.quantity }
    val totalPrice = cart.values.sumOf { it.menuItem.price * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cantina/Bar") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    BadgedBox(
                        badge = {
                            if (totalItems > 0) {
                                Badge {
                                    Text("$totalItems")
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (cart.isNotEmpty()) {
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "$totalItems items",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "€${String.format("%.2f", totalPrice)}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AccentTeal
                                )
                            }

                            Button(
                                onClick = {
                                    navController.navigate(
                                        "qr_code/${totalPrice.toFloat()}/$totalItems"
                                    )
                                },
                                modifier = Modifier
                                    .height(56.dp)
                                    .widthIn(min = 140.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AccentTeal
                                )
                            ) {
                                Text("Checkout", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(menuItems, key = { "${it.id}_$cartVersion" }) { item ->
                FoodMenuItem(
                    item = item,
                    quantity = cart[item.id]?.quantity ?: 0,
                    onAdd = {
                        if (cart[item.id] == null) {
                            cart[item.id] = CartItem(item, 1)
                        } else {
                            cart[item.id]?.quantity = (cart[item.id]?.quantity ?: 0) + 1
                        }
                        cartVersion++
                    },
                    onRemove = {
                        val current = cart[item.id]
                        if (current != null) {
                            if (current.quantity > 1) {
                                current.quantity--
                            } else {
                                cart.remove(item.id)
                            }
                        }
                        cartVersion++
                    }
                )
            }
        }
    }
}

@Composable
fun FoodMenuItem(
    item: MenuItem,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (quantity > 0) {
                    Modifier.border(
                        BorderStroke(3.dp, AccentTeal),
                        shape = MaterialTheme.shapes.medium
                    )
                } else Modifier
            ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(BackgroundLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.imageResId),
                    contentDescription = context.getString(item.nameResId),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = context.getString(item.nameResId),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2
            )

            Text(
                text = "€${String.format("%.2f", item.price)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AccentTeal
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (quantity == 0) {
                Button(
                    onClick = onAdd,
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentTeal
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", fontSize = 14.sp)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.LightGray, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Remove",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = "$quantity",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentTeal
                    )

                    IconButton(
                        onClick = onAdd,
                        modifier = Modifier
                            .size(32.dp)
                            .background(AccentTeal, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun getMenuItems(): List<MenuItem> {
    return listOf(
        MenuItem(1, R.string.menu_cookies, R.drawable.cookies, 1.50, MenuCategory.SNACKS),
        MenuItem(2, R.string.menu_croissant, R.drawable.croissant, 1.80, MenuCategory.SNACKS),
        MenuItem(3, R.string.menu_tosta_mista, R.drawable.tosta_mista, 2.50, MenuCategory.MEALS),
        MenuItem(4, R.string.menu_brownie, R.drawable.brownie, 2.00, MenuCategory.SNACKS),
        MenuItem(5, R.string.menu_sandes_mista, R.drawable.sandes_mista, 3.00, MenuCategory.MEALS),
        MenuItem(6, R.string.menu_torradas, R.drawable.torradas, 1.50, MenuCategory.SNACKS)
    )
}

@Preview(showBackground = true)
@Composable
fun FoodMenuScreenPreview() {
    com.example.projeto_3o_semestre_pdm.ui.theme.IADEGOTheme {
        FoodMenuScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun FoodMenuItemPreview() {
    com.example.projeto_3o_semestre_pdm.ui.theme.IADEGOTheme {
        FoodMenuItem(
            item = MenuItem(1, R.string.menu_cookies, R.drawable.cookies, 1.50, MenuCategory.SNACKS),
            quantity = 2,
            onAdd = {},
            onRemove = {}
        )
    }
}