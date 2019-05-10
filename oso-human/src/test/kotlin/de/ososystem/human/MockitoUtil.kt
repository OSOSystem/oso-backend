package de.ososystem.human

import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

fun <T> verifyOnce(mock: T) = Mockito.verify(mock, once())
fun once() = Mockito.times(1)

fun <T> any(): T = Mockito.any<T>()

fun <T> eq(value: T): T = ArgumentMatchers.eq<T>(value)

fun <T> argThat(matcher: ArgumentMatcher<T>): T = ArgumentMatchers.argThat<T>(matcher)

inline fun <reified T> mock() = Mockito.mock(T::class.java)

inline fun <reified T> captorForClass() = ArgumentCaptor.forClass<T, T>(T::class.java)