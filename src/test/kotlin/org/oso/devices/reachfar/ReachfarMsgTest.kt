package org.oso.devices.reachfar

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

internal class ReachfarMsgTest {
    @TestFactory
    fun testExtractMsg() =
        listOf(
            // no marks
            "anyinput" to "",
            // missing endmark
            "${ReachfarMsg.startMark}" to "",
            "anyinput${ReachfarMsg.startMark}anyinput" to "",
            // missing startmark
            "${ReachfarMsg.endMark}" to "",
            "anyinput${ReachfarMsg.endMark}anyinput" to "",
            // no endmark after startmark
            "${ReachfarMsg.endMark}${ReachfarMsg.startMark}" to "",
            "anyinput${ReachfarMsg.endMark}${ReachfarMsg.startMark}anyinput" to "",
            "anyinput${ReachfarMsg.endMark}anyinput${ReachfarMsg.startMark}anyinput" to "",
            // simple msg
            "${ReachfarMsg.startMark}${ReachfarMsg.endMark}" to "${ReachfarMsg.startMark}${ReachfarMsg.endMark}",
            "anyinput${ReachfarMsg.startMark}${ReachfarMsg.endMark}anyinput" to "${ReachfarMsg.startMark}${ReachfarMsg.endMark}",
            "${ReachfarMsg.startMark}anyinput${ReachfarMsg.endMark}" to "${ReachfarMsg.startMark}anyinput${ReachfarMsg.endMark}",
            "anyinput${ReachfarMsg.startMark}anyinput${ReachfarMsg.endMark}anyinput" to "${ReachfarMsg.startMark}anyinput${ReachfarMsg.endMark}",
            // msg surrounded by incomplete messages
            "${ReachfarMsg.endMark}${ReachfarMsg.startMark}${ReachfarMsg.endMark}${ReachfarMsg.startMark}" to "${ReachfarMsg.startMark}${ReachfarMsg.endMark}",
            "anyinput${ReachfarMsg.endMark}anyinput${ReachfarMsg.startMark}anyinput${ReachfarMsg.endMark}anyinput${ReachfarMsg.startMark}" to "${ReachfarMsg.startMark}anyinput${ReachfarMsg.endMark}",
            // extract the first msg only
            "${ReachfarMsg.startMark}${ReachfarMsg.endMark}${ReachfarMsg.startMark}anyinput${ReachfarMsg.endMark}" to "${ReachfarMsg.startMark}${ReachfarMsg.endMark}",
            "anyinput${ReachfarMsg.startMark}${ReachfarMsg.endMark}anyinput${ReachfarMsg.startMark}anyinput${ReachfarMsg.endMark}anyinput" to "${ReachfarMsg.startMark}${ReachfarMsg.endMark}",
            "anyinput${ReachfarMsg.endMark}anyinput${ReachfarMsg.startMark}${ReachfarMsg.endMark}anyinput${ReachfarMsg.startMark}anyinput${ReachfarMsg.endMark}anyinput${ReachfarMsg.startMark}anyinput" to "${ReachfarMsg.startMark}${ReachfarMsg.endMark}"
        )
        .map { (input, expected) ->
            DynamicTest.dynamicTest("Msg<$expected> should have been extracted from<$input>") {
                Assertions.assertEquals(expected, ReachfarMsg.extractMsg(input))
            }
        }

    @TestFactory
    fun testIsReachfarMsg() = listOf(
        // empty
        "" to false,
        // missing endmark
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE" to false,
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.delimiter}anything${ReachfarMsg.delimiter}anything2${ReachfarMsg.delimiter}anything3" to false,
        // missing startmark
        "HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.endMark}" to false,
        "HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.delimiter}anything${ReachfarMsg.delimiter}anything2${ReachfarMsg.delimiter}anything3${ReachfarMsg.endMark}" to false,
        // empty needed field
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}${ReachfarMsg.endMark}" to false,
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}${ReachfarMsg.delimiter}TYPE${ReachfarMsg.endMark}" to false,
        "${ReachfarMsg.startMark}${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.endMark}" to false,
        // too few fields
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.endMark}" to false,
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.endMark}" to false,
        "${ReachfarMsg.startMark}${ReachfarMsg.endMark}" to false,

        // valid msg with least amount of fields
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.endMark}" to true,
        // valid msg with 1 additional parameter
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.delimiter}anything${ReachfarMsg.endMark}" to true,
        // valid msg with 3 additional parameter
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.delimiter}anything${ReachfarMsg.delimiter}anything2${ReachfarMsg.delimiter}anything3${ReachfarMsg.endMark}" to true,
        // empty random field
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.delimiter}anything${ReachfarMsg.delimiter}anything2${ReachfarMsg.delimiter}${ReachfarMsg.endMark}" to true,
        // too much input at start
        "anything${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.endMark}" to true,
        // too much input at end
        "${ReachfarMsg.startMark}HQ${ReachfarMsg.delimiter}0123456789${ReachfarMsg.delimiter}TYPE${ReachfarMsg.endMark}anything" to true
    )
    .map { (input, expected) ->
        DynamicTest.dynamicTest("Msg<$input> should have " + (if(expected) "" else "not") + "been recognized as Reachfarmsg") {
            Assertions.assertEquals(expected, ReachfarMsg.isReachfarMsg(input))
        }
    }
}