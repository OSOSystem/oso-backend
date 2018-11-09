package org.oso

import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

fun <T> any(): T = Mockito.any<T>()

fun <T> eq(value: T): T = ArgumentMatchers.eq<T>(value)

fun <T> argThat(matcher: ArgumentMatcher<T>): T = ArgumentMatchers.argThat<T>(matcher)