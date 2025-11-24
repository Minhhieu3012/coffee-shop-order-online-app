package vn.edu.ut.hieupm9898.customermobile.features.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

/**
 * Màn hình điều phối 3 màn hình Onboarding
 * Sử dụng HorizontalPager để swipe giữa các màn hình
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPagerScreen(
    onGetStartedClick: () -> Unit
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        count = 3,
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> Onboarding1Screen(
                onSkip = onGetStartedClick, // Skip = đi thẳng đến Auth
                onNext = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                onGetStartedClick = onGetStartedClick
            )
            1 -> Onboarding2Screen(
                onSkip = onGetStartedClick,
                onNext = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                },
                onGetStartedClick = onGetStartedClick
            )
            2 -> Onboarding3Screen(
                onSkip = onGetStartedClick,
                onNext = onGetStartedClick, // Màn cuối = Get Started
                onGetStartedClick = onGetStartedClick
            )
        }
    }
}