package com.kwh.almuniconnect.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun AlumniCountBanner(
    totalAlumni: Long,
    activeThisWeek: Int = (totalAlumni / 4).toInt(), // 🔥 dynamic estimate
    isTitle: Boolean
) {

    if (totalAlumni <= 0) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFC) // soft premium bg
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🎓 Icon
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFE0E7FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("🎓", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                // 🔥 MAIN LINE (strong alumni feel)
                Text(
                    text = "$totalAlumni+ Harcourtians • $activeThisWeek active this week",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                // ❤️ Emotional line
                val subText = if (isTitle) {
                    "You’re part of this growing legacy ❤️"
                } else {
                    "Join and reconnect with your alumni network"
                }

                Text(
                    text = subText,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
//@Composable
//fun AlumniCountBanner(totalAlumni: Long,isTitle:Boolean) {
//
//    if (totalAlumni > 0) {
//
//        val message = when {
//            totalAlumni < 100 -> "🌱 Growing alumni community"
//            totalAlumni < 500 -> "🚀 $totalAlumni+ alumni joined"
//            else -> "🔥 $totalAlumni+ strong alumni network"
//        }
//
//        Card(
//            colors = CardDefaults.cardColors(
//                containerColor = Color.White
//            ),
//            shape = RoundedCornerShape(16.dp),
//            elevation = CardDefaults.cardElevation(1.dp),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp)
//        ) {
//
//            Row(
//                modifier = Modifier
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
//                // 🎓 Icon Circle (NEW)
//                Box(
//                    modifier = Modifier
//                        .size(40.dp)
//                        .background(Color.White, CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("🎓", fontSize = 20.sp)
//                }
//
//                Spacer(modifier = Modifier.width(12.dp))
//
//                Column(modifier = Modifier.weight(1f)) {
//
//                    // 🔥 Dynamic headline
//                    Text(
//                        text = message,
//                        fontWeight = FontWeight.SemiBold,
//                        color = Color(0xFF1D4ED8)
//                    )
//
//                    Spacer(modifier = Modifier.height(2.dp))
//                    if (isTitle) {
//                        Text(
//                            text = "You’re part of a $totalAlumni+ strong alumni network",
//                            fontSize = 12.sp,
//                            color = Color(0xFF6B7280)
//                        )
//
//                    } else {
//                        Text(
//                            text = "$totalAlumni+ alumni are already here — join them today",
//                            fontSize = 12.sp,
//                            color = Color(0xFF6B7280)
//                        )
//                    }
//
//
//                }
//            }
//        }
//    }
//}
