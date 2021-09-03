package com.example.modular

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberImagePainter
import com.example.annotation.Router
import com.example.common.net.api.RetrofitClient
import com.example.common.router.RouterManager
import com.example.modular.BuildConfig
import com.example.modular.bean.DataBean
import com.example.modular.ui.theme.ModularTheme
import com.example.modular.viewModel.MainViewModel
import com.example.webview.WebViewActivity
import com.example.webview.utils.ANDROID_ASSET_URI
import com.example.webview.utils.IS_SHOW_ACTION_BAR
import com.example.webview.utils.TITLE
import com.example.webview.utils.URL
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Router(path = "app/MainActivity")
class MainActivity : ComponentActivity() {

    private val mMainViewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.setup(BuildConfig.HOST, listOf())
        mMainViewModel.getNewsData("top")
        setContent {
//            ModularTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
                ) {
                    BottomNavigation(mMainViewModel)
                }
//            }
        }
    }


}


fun startDemoHtml(context: Context) {
    val intent = Intent(context, WebViewActivity::class.java)
    intent.apply {
        putExtra(TITLE,"本地测试")
        putExtra(URL, ANDROID_ASSET_URI + "html/demo.html")
        putExtra(IS_SHOW_ACTION_BAR,false)
    }
    context.startActivity(intent)
}

fun startWebActivity(context: Context,url: String,isShowHead: Boolean = true) {
    val bundle = Bundle()
    bundle.apply {
        putSerializable(TITLE,"")
        putSerializable(URL,url)
        putBoolean(IS_SHOW_ACTION_BAR,isShowHead)
    }
    RouterManager.startActivity("webview/WebViewActivity",context,bundle)
}


@Composable
fun BottomNavigation(mainViewModel: MainViewModel){

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val hasData = remember { mutableStateOf( false) }

    val context = LocalContext.current

    mainViewModel.newsData.observe(context as LifecycleOwner){
        hasData.value = true
    }

    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("首页", "个人中心")

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color(0xFFFFFFFF),
                title = {
                    Text("NEWS")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(Icons.Filled.Menu, null)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigation (
                backgroundColor = Color(0xFFFFFFFF)
                    ){
                items.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        icon = { Icon(if(index==0)
                            Icons.Filled.Home else Icons.Filled.Person
                            , contentDescription = null) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        },
        drawerContent = {
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.TopStart){
                    AppDrawer(jumpToWebActivity = {
                        startWebActivity(context,it)
                    },closeDrawer = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    })
            }
        },
        scaffoldState = scaffoldState,
    ){
        if (selectedItem == 0){
            LazyColumn(){
                if (hasData.value){
                    mainViewModel.newsData.value?.result?.data?.let {
                        items(it){item ->
                            NewsItem(item,context)
                        }
                    }
                }
            }
        }else{

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "html_test",Modifier.clickable {
                    startDemoHtml(context)
                })
            }
        }
    }
}


@Composable
fun AppDrawer(
    jumpToWebActivity: (url: String) -> Unit,
    closeDrawer: () -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }

    var text by remember{ mutableStateOf("")}


    Column(modifier = Modifier
        .fillMaxHeight()
        .wrapContentWidth()
        .background(Color.White)) {

        DrawerButton(
            icon = Icons.Filled.Send,
            label = "跳转至浏览器",
            isSelected = false,
            action = {
                openDialog.value = true
                closeDrawer()
            }
        )

        DrawerButton(
            icon = Icons.Filled.Share,
            label = "分享",
            isSelected = false,
            action = {
                closeDrawer()
            }
        )
    }



    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // 当用户点击对话框以外的地方或者按下系统返回键将会执行的代码
                openDialog.value = false
            },
            title = {
                Text(
                    text = "请输入网址",
                    fontWeight = FontWeight.W700,
                    style = MaterialTheme.typography.h6
                )
            },
            text = {
                Column() {
                    Text(text = "", modifier = Modifier.height(10.dp))
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        placeholder = {
                            Text("别手抖哦~")
                        },
                        singleLine = true
                    )
                }

            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        jumpToWebActivity(text)
                    },
                ) {
                    Text(
                        "确认",
                        fontWeight = FontWeight.W700,
                        color = Color(0xFF000000)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(
                        "取消",
                        fontWeight = FontWeight.W700,
                        color = Color(0xFF000000)
                    )
                }
            }
        )
    }
}


@Composable
private fun DrawerButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colors

    val imageAlpha = if (isSelected) 1f else 0.6f

    val textIconColor = if (isSelected) colors.primary else colors.onSurface.copy(alpha = 0.6f)

    val backgroundColor = if (isSelected) colors.primary.copy(alpha = 0.12f) else Color.Transparent

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()

    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = null, // decorative
                    colorFilter = ColorFilter.tint(textIconColor),
                    alpha = imageAlpha
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.body2,
                    color = textIconColor
                )
            }
        }
    }
}


@Composable
fun NewsItem(data: DataBean,context: Context){
    Surface(
        elevation = 5.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .height(100.dp)
            .clickable {
                data.url?.let {
                    startWebActivity(context, it)
                }
            }
    ) {
        Row(
            modifier = Modifier.padding(all = 5.dp)
        ) {
            Image(
                painter = rememberImagePainter(data = data.thumbnail_pic_s ?: ""),
                contentDescription = "profile picture",
                modifier = Modifier
                    .size(width = 160.dp, height = 90.dp)
            )
            ConstraintLayout(
                Modifier
                    .padding(start = 5.dp)
                    .fillMaxHeight()){
                val (title, info) = createRefs()


                Text(
                    text = data.title ?: "",
                    color = Color(0xff000000),
                    modifier = Modifier.constrainAs(title){
                        top.linkTo(parent.top)
                    },
                    maxLines = 2,
                    style = TextStyle(
                        fontSize = 16.sp,
                    )
                )
                Row ( modifier = Modifier.constrainAs(info){
                    bottom.linkTo(parent.bottom)
                }){
                    Text(
                        text = data.author_name ?: "",
                        color = Color(0x55000000),
                        style = TextStyle(
                            fontSize = 10.sp,
                        ),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .animateContentSize()
                            .weight(1f)
                    )
                    Text(
                        text = data.date?.format(SimpleDateFormat("yy-MM-dd HH:mm:ss")) ?: "",
                        color = Color(0x55000000),
                        style = TextStyle(
                            fontSize = 10.sp,
                        ),
                        textAlign = TextAlign.End,
                        // Composable 大小的动画效果
                        modifier = Modifier
                            .animateContentSize()
                            .weight(1f)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}