package org.oso.devices.reachfar

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

internal class ReachfarMsgTypeTest {
    @TestFactory
    fun testTypeOfMsg() =
        listOf(
            "${ReachfarMsg.startMark}XX${ReachfarMsg.delimiter}YYYYYYYYYY${ReachfarMsg.delimiter}V1${ReachfarMsg.endMark}" to ReachFarMsgType.V1,
            "${ReachfarMsg.startMark}XX${ReachfarMsg.delimiter}YYYYYYYYYY${ReachfarMsg.delimiter}V4${ReachfarMsg.endMark}" to ReachFarMsgType.V4,
            "${ReachfarMsg.startMark}XX${ReachfarMsg.delimiter}YYYYYYYYYY${ReachfarMsg.delimiter}VI1${ReachfarMsg.endMark}" to ReachFarMsgType.VI1,
            "${ReachfarMsg.startMark}XX${ReachfarMsg.delimiter}YYYYYYYYYY${ReachfarMsg.delimiter}V8${ReachfarMsg.endMark}" to ReachFarMsgType.V8,
            "${ReachfarMsg.startMark}XX${ReachfarMsg.delimiter}YYYYYYYYYY${ReachfarMsg.delimiter}VI2${ReachfarMsg.endMark}" to ReachFarMsgType.VI2,
            "${ReachfarMsg.startMark}XX${ReachfarMsg.delimiter}YYYYYYYYYY${ReachfarMsg.delimiter}LINK${ReachfarMsg.endMark}" to ReachFarMsgType.LINK,
            "${ReachfarMsg.startMark}XX${ReachfarMsg.delimiter}YYYYYYYYYY${ReachfarMsg.delimiter}NBR${ReachfarMsg.endMark}" to ReachFarMsgType.NBR,
            "${ReachfarMsg.startMark}XX${ReachfarMsg.delimiter}YYYYYYYYYY${ReachfarMsg.delimiter}ANY${ReachfarMsg.endMark}" to null
        )
        .map { (input, expected) ->
            DynamicTest.dynamicTest("Msg<$input> should have been recognized as ${expected?.typeString}") {
                Assertions.assertEquals(expected, ReachFarMsgType.typeOf(ReachfarMsg(input)))
            }
        }

    @Test
    fun `test parse NBR with 1 cell and num 0`() {
        val msg = ReachfarMsg(
            "HQ",
            "1234567890",
            "NBR",
            listOf(
                "121530",
                "123",
                "456",
                "0",
                "0", // NUM
                "12345",
                "67890",
                "98",
                "160398",
                "FFE7FBFF"
            )
        )

        val data = ReachFarMsgType.NBR.parse(msg)

        Assertions.assertEquals("121530", data["HHMMSS"])
        Assertions.assertEquals("123", data["MCC"])
        Assertions.assertEquals("456", data["MNC"])
        Assertions.assertEquals("0", data["TA"])
        Assertions.assertEquals("0", data["NUM"])
        Assertions.assertEquals("12345", data["RXLEV0"])
        Assertions.assertEquals("67890", data["LAC0"])
        Assertions.assertEquals("98", data["CID0"])
        Assertions.assertEquals("160398", data["DDMMYY"])
        Assertions.assertEquals("FFE7FBFF", data["vehicle_status"])
    }


    @Test
    fun `test parse NBR with 1 cell`() {
        val msg = ReachfarMsg(
            "HQ",
            "1234567890",
            "NBR",
            listOf(
                "121530",
                "123",
                "456",
                "0",
                "1", // NUM
                "12345",
                "67890",
                "98",
                "160398",
                "FFE7FBFF"
            )
        )

        val data = ReachFarMsgType.NBR.parse(msg)

        Assertions.assertEquals("121530", data["HHMMSS"])
        Assertions.assertEquals("123", data["MCC"])
        Assertions.assertEquals("456", data["MNC"])
        Assertions.assertEquals("0", data["TA"])
        Assertions.assertEquals("1", data["NUM"])
        Assertions.assertEquals("12345", data["RXLEV0"])
        Assertions.assertEquals("67890", data["LAC0"])
        Assertions.assertEquals("98", data["CID0"])
        Assertions.assertEquals("160398", data["DDMMYY"])
        Assertions.assertEquals("FFE7FBFF", data["vehicle_status"])
    }


    @Test
    fun `test parse NBR with 2 cells`() {
        val msg = ReachfarMsg(
            "HQ",
            "1234567890",
            "NBR",
            listOf(
                "121530",
                "123",
                "456",
                "0",
                "2", // NUM
                "12345",
                "67890",
                "98",
                "54321",
                "09876",
                "30",
                "160398",
                "FFE7FBFF"
            )
        )

        val data = ReachFarMsgType.NBR.parse(msg)

        Assertions.assertEquals("121530", data["HHMMSS"])
        Assertions.assertEquals("123", data["MCC"])
        Assertions.assertEquals("456", data["MNC"])
        Assertions.assertEquals("0", data["TA"])
        Assertions.assertEquals("2", data["NUM"])
        Assertions.assertEquals("12345", data["RXLEV0"])
        Assertions.assertEquals("67890", data["LAC0"])
        Assertions.assertEquals("98", data["CID0"])
        Assertions.assertEquals("54321", data["RXLEV1"])
        Assertions.assertEquals("09876", data["LAC1"])
        Assertions.assertEquals("30", data["CID1"])
        Assertions.assertEquals("160398", data["DDMMYY"])
        Assertions.assertEquals("FFE7FBFF", data["vehicle_status"])
    }


    @Test
    fun `test parse NBR with 5 cells`() {
        val msg = ReachfarMsg(
            "HQ",
            "1234567890",
            "NBR",
            listOf(
                "121530",
                "123",
                "456",
                "0",
                "5", // NUM
                "12345",
                "67890",
                "98",
                "54321",
                "09876",
                "30",
                "75391",
                "68420",
                "26",
                "01478",
                "36987",
                "12",
                "98642",
                "34675",
                "100",
                "160398",
                "FFE7FBFF"
            )
        )

        val data = ReachFarMsgType.NBR.parse(msg)

        Assertions.assertEquals("121530", data["HHMMSS"])
        Assertions.assertEquals("123", data["MCC"])
        Assertions.assertEquals("456", data["MNC"])
        Assertions.assertEquals("0", data["TA"])
        Assertions.assertEquals("5", data["NUM"])
        Assertions.assertEquals("12345", data["RXLEV0"])
        Assertions.assertEquals("67890", data["LAC0"])
        Assertions.assertEquals("98", data["CID0"])
        Assertions.assertEquals("54321", data["RXLEV1"])
        Assertions.assertEquals("09876", data["LAC1"])
        Assertions.assertEquals("30", data["CID1"])
        Assertions.assertEquals("75391", data["RXLEV2"])
        Assertions.assertEquals("68420", data["LAC2"])
        Assertions.assertEquals("26", data["CID2"])
        Assertions.assertEquals("01478", data["RXLEV3"])
        Assertions.assertEquals("36987", data["LAC3"])
        Assertions.assertEquals("12", data["CID3"])
        Assertions.assertEquals("98642", data["RXLEV4"])
        Assertions.assertEquals("34675", data["LAC4"])
        Assertions.assertEquals("100", data["CID4"])
        Assertions.assertEquals("160398", data["DDMMYY"])
        Assertions.assertEquals("FFE7FBFF", data["vehicle_status"])
    }

    @Test
    fun testParseLink() {
        val msg = ReachfarMsg(
            "HQ",
            "1234567890",
            "LINK",
            listOf(
                "182635",
                "100",
                "0",
                "98",
                "0",
                "0",
                "121314",
                "FFE7FBFF"
            )
        )
        val data = ReachFarMsgType.LINK.parse(msg)

        Assertions.assertEquals("182635", data["HHMMSS"])
        Assertions.assertEquals("100", data["GSM"])
        Assertions.assertEquals("0", data["GPS"])
        Assertions.assertEquals("98", data["BAT"])
        Assertions.assertEquals("0", data["STEP"])
        Assertions.assertEquals("0", data["TURNOVER"])
        Assertions.assertEquals("121314", data["DDMMYY"])
        Assertions.assertEquals("FFE7FBFF", data["tracker_status"])
    }
}