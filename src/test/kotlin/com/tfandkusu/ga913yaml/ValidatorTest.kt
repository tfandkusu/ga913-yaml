package com.tfandkusu.ga913yaml

import com.tfandkusu.ga913yaml.model.Action
import com.tfandkusu.ga913yaml.model.Parameter
import com.tfandkusu.ga913yaml.model.ParameterType
import com.tfandkusu.ga913yaml.model.Screen
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class ValidatorTest {
    @Test
    fun checkScreenEventNameLength() {
        val screens =
            listOf(
                Screen(
                    description = "40文字を超えない画面遷移イベント",
                    className = "Screen1",
                    eventName = "a".repeat(40),
                ),
                Screen(
                    description = "40文字を超える画面遷移イベント",
                    className = "Screen2",
                    eventName = "b".repeat(41),
                ),
            )
        try {
            Validator.validate(screens)
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("画面遷移イベント Screen2 は40文字を超えています。", e.message)
        }
    }

    @Test
    fun checkActionEventNameLength() {
        val screens =
            listOf(
                Screen(
                    description = "画面1",
                    className = "Screen1",
                    eventName = "a".repeat(20),
                    actions =
                        listOf(
                            Action(
                                description = "40文字を超えない画面内操作イベント",
                                className = "Action1",
                                eventName = "b".repeat(20),
                            ),
                            Action(
                                description = "40文字を超える画面内操作イベント",
                                className = "Action2",
                                eventName = "c".repeat(21),
                            ),
                        ),
                ),
            )
        try {
            Validator.validate(screens)
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "画面内操作イベント Screen1.Action2 は40文字を超えています。",
                e.message,
            )
        }
    }

    @Test
    fun checkEventParameterKeyLength() {
        val screens =
            listOf(
                Screen(
                    description = "画面1",
                    className = "Screen1",
                    eventName = "Scene1",
                    actions =
                        listOf(
                            Action(
                                description = "画面内操作1",
                                className = "Action1",
                                eventName = "Action1",
                                parameters =
                                    listOf(
                                        Parameter(
                                            description = "40文字を超えないパラメータキー",
                                            propertyName = "param1",
                                            eventParameterKey = "a".repeat(40),
                                            type = ParameterType.STRING,
                                        ),
                                        Parameter(
                                            description = "40文字を超えるパラメータキー",
                                            propertyName = "param2",
                                            eventParameterKey = "b".repeat(41),
                                            type = ParameterType.STRING,
                                        ),
                                    ),
                            ),
                        ),
                ),
            )
        try {
            Validator.validate(screens)
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals(
                "画面内操作イベント Screen1.Action1 のパラメータキー param2 は40文字を超えています。",
                e.message,
            )
        }
    }

    @Test
    fun checkEventCountWithoutException() {
        val screens =
            (0 until 250).map {
                Screen(
                    description = "画面$it",
                    className = "Screen$it",
                    eventName = "Scene$it",
                    actions =
                        listOf(
                            Action(
                                description = "画面内操作1",
                                className = "Action1",
                                eventName = "Action1",
                                isConversionEvent = true,
                            ),
                        ),
                    isConversionEvent = true,
                )
            }
        Validator.validate(screens)
    }

    @Test
    fun checkEventCountWithException() {
        val screens =
            (0 until 250).map {
                Screen(
                    description = "画面$it",
                    className = "Screen$it",
                    eventName = "Scene$it",
                    actions =
                        listOf(
                            Action(
                                description = "画面内操作1",
                                className = "Action1",
                                eventName = "Action1",
                                isConversionEvent = true,
                            ),
                        ),
                    isConversionEvent = true,
                )
            } +
                Screen(
                    description = "画面250",
                    className = "Screen250",
                    eventName = "Scene250",
                    isConversionEvent = true,
                )
        try {
            Validator.validate(screens)
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("イベント種類数が500を超えています。", e.message)
        }
    }

    @Test
    fun checkScreenEventNameDuplicate() {
        val screens =
            listOf(
                Screen(
                    description = "画面1",
                    className = "Screen1",
                    eventName = "Screen1",
                ),
                Screen(
                    description = "画面2",
                    className = "Screen2",
                    eventName = "Screen1",
                ),
            )
        try {
            Validator.validate(screens)
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("画面遷移イベント Screen2 は他のイベントと重複しています。", e.message)
        }
    }

    @Test
    fun checkActionEventNameDuplicate() {
        val screens =
            listOf(
                Screen(
                    description = "画面1",
                    className = "Screen1",
                    eventName = "Screen1",
                    actions =
                        listOf(
                            Action(
                                description = "画面内操作1",
                                className = "Action1",
                                eventName = "Action1",
                            ),
                            Action(
                                description = "画面内操作2",
                                className = "Action2",
                                eventName = "Action1",
                            ),
                        ),
                ),
            )
        try {
            Validator.validate(screens)
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("画面内操作イベント Screen1.Action2 は他のイベントと重複しています。", e.message)
        }
    }

    @Test
    fun checkJoinedEventNameDuplicate() {
        val screens =
            listOf(
                Screen(
                    description = "画面1",
                    className = "Screen1",
                    eventName = "Screen1",
                    actions =
                        listOf(
                            Action(
                                description = "画面内操作1",
                                className = "Action1",
                                eventName = "Action1",
                            ),
                        ),
                ),
                Screen(
                    description = "画面2",
                    className = "Screen2",
                    eventName = "Screen1Action1",
                ),
            )
        try {
            Validator.validate(screens)
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("画面遷移イベント Screen2 は他のイベントと重複しています。", e.message)
        }
    }
}
