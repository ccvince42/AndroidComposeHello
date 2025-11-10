package com.example.hello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hello.ui.theme.HelloTheme
import kotlinx.coroutines.launch

// ----------------------------
// 資料類別，包含展開狀態
data class Message(
    val author: String,
    val body: String,
    var isExpanded: MutableState<Boolean> = mutableStateOf(false)
)

// ----------------------------
// 單一訊息卡片
@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.chat_icon),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        val surfaceColor by animateColorAsState(
            if (msg.isExpanded.value) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surface
        )

        Column(modifier = Modifier.clickable { msg.isExpanded.value = !msg.isExpanded.value }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (msg.isExpanded.value) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// ----------------------------
// 顯示訊息清單
@Composable
fun Conversation(messages: List<Message>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(50.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(messages) { message ->
                MessageCard(msg = message)
            }
        }
    }
}

// ----------------------------
// 假資料
val SampleData = mutableStateListOf(
    Message("Alice", "Hey, have you seen the new Jetpack Compose tutorial?"),
    Message("Bob", "Yeah! It's really fun to use and very declarative."),
    Message("Charlie", "I love how animations are so easy with Compose!"),
    Message("Diana", "Same here! The Surface composable makes styling smooth."),
    Message("Qoo", "Good morning, The Surface composable makes styling smooth."),
    Message("Bork", "Good afternoon The Surface composable makes styling smooth."),
    Message("Austin", "Good evening The Surface composable makes styling smooth."),
    Message("John", "Good night The Surface composable makes styling smooth."),
    Message("Jolin", "I am a student. The Surface composable makes styling smooth."),
    Message("Grace", "I am a teacher The Surface composable makes styling smooth."),
    Message("Cince", "Who are you? The Surface composable makes styling smooth."),
    Message("Stakin", "I am a engineer. The Surface composable makes styling smooth."),
    Message("Aellen", "Oh my god. The Surface composable makes styling smooth."),
    Message("Joe", "There is no room. The Surface composable makes styling smooth."),
    Message("Brian", "Are you OK? The Surface composable makes styling smooth."),
    Message("Billy", "I am fine. The Surface composable makes styling smooth.")
)

// ----------------------------
// 側滑頁面 + 指示器
@Composable
fun SwipePagesWithIndicator(messages: List<Message>) {
    val pagerState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> Conversation(messages)
                1 -> Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFA500))
                ) {
                    Text(
                        text = "第二頁 - 橘色背景",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        // Dots Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(2) { index ->
                val color = if (pagerState.currentPage == index) Color.White else Color.Gray
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .padding(4.dp)
                        .background(color = color, shape = CircleShape)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                )
            }
        }
    }
}

// ----------------------------
// MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloTheme {
                SwipePagesWithIndicator(SampleData)
            }
        }
    }
}

// ----------------------------
// 預覽
@Preview(showBackground = true)
@Composable
fun PreviewConversation() {
    HelloTheme {
        SwipePagesWithIndicator(SampleData)
    }
}