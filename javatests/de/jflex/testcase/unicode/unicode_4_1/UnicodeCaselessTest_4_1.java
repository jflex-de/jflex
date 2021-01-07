/*
 * Copyright (C) 2021 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.jflex.testcase.unicode.unicode_4_1;

import static com.google.common.truth.Truth.assertWithMessage;

import de.jflex.testing.unicodedata.UnicodeDataScanners;
import de.jflex.util.scanner.ScannerFactory;
import javax.annotation.Generated;
import org.junit.Test;

/** Test Tests the {@code %caseless} directive for Unicode 4.1. */
@Generated("de.jflex.migration.unicodedatatest.testcaseless.UnicodeCaseselessTestGenerator")
public class UnicodeCaselessTest_4_1 {
  @Test
  public void caseless() throws Exception {
    UnicodeCaseless_4_1 scanner =
        UnicodeDataScanners.scanAllCodepoints(
            ScannerFactory.of(UnicodeCaseless_4_1::new),
            UnicodeCaseless_4_1.YYEOF,
            UnicodeDataScanners.Dataset.ALL);
    assertWithMessage("Character 0x0041 matches caselessly 0x0041")
        .that(scanner.getPropertyValue(65))
        .isEqualTo(65);
    assertWithMessage("Character 0x0042 matches caselessly 0x0042")
        .that(scanner.getPropertyValue(66))
        .isEqualTo(66);
    assertWithMessage("Character 0x0043 matches caselessly 0x0043")
        .that(scanner.getPropertyValue(67))
        .isEqualTo(67);
    assertWithMessage("Character 0x0044 matches caselessly 0x0044")
        .that(scanner.getPropertyValue(68))
        .isEqualTo(68);
    assertWithMessage("Character 0x0045 matches caselessly 0x0045")
        .that(scanner.getPropertyValue(69))
        .isEqualTo(69);
    assertWithMessage("Character 0x0046 matches caselessly 0x0046")
        .that(scanner.getPropertyValue(70))
        .isEqualTo(70);
    assertWithMessage("Character 0x0047 matches caselessly 0x0047")
        .that(scanner.getPropertyValue(71))
        .isEqualTo(71);
    assertWithMessage("Character 0x0048 matches caselessly 0x0048")
        .that(scanner.getPropertyValue(72))
        .isEqualTo(72);
    assertWithMessage("Character 0x0049 matches caselessly 0x0049")
        .that(scanner.getPropertyValue(73))
        .isEqualTo(73);
    assertWithMessage("Character 0x004a matches caselessly 0x004a")
        .that(scanner.getPropertyValue(74))
        .isEqualTo(74);
    assertWithMessage("Character 0x004b matches caselessly 0x004b")
        .that(scanner.getPropertyValue(75))
        .isEqualTo(75);
    assertWithMessage("Character 0x004c matches caselessly 0x004c")
        .that(scanner.getPropertyValue(76))
        .isEqualTo(76);
    assertWithMessage("Character 0x004d matches caselessly 0x004d")
        .that(scanner.getPropertyValue(77))
        .isEqualTo(77);
    assertWithMessage("Character 0x004e matches caselessly 0x004e")
        .that(scanner.getPropertyValue(78))
        .isEqualTo(78);
    assertWithMessage("Character 0x004f matches caselessly 0x004f")
        .that(scanner.getPropertyValue(79))
        .isEqualTo(79);
    assertWithMessage("Character 0x0050 matches caselessly 0x0050")
        .that(scanner.getPropertyValue(80))
        .isEqualTo(80);
    assertWithMessage("Character 0x0051 matches caselessly 0x0051")
        .that(scanner.getPropertyValue(81))
        .isEqualTo(81);
    assertWithMessage("Character 0x0052 matches caselessly 0x0052")
        .that(scanner.getPropertyValue(82))
        .isEqualTo(82);
    assertWithMessage("Character 0x0053 matches caselessly 0x0053")
        .that(scanner.getPropertyValue(83))
        .isEqualTo(83);
    assertWithMessage("Character 0x0054 matches caselessly 0x0054")
        .that(scanner.getPropertyValue(84))
        .isEqualTo(84);
    assertWithMessage("Character 0x0055 matches caselessly 0x0055")
        .that(scanner.getPropertyValue(85))
        .isEqualTo(85);
    assertWithMessage("Character 0x0056 matches caselessly 0x0056")
        .that(scanner.getPropertyValue(86))
        .isEqualTo(86);
    assertWithMessage("Character 0x0057 matches caselessly 0x0057")
        .that(scanner.getPropertyValue(87))
        .isEqualTo(87);
    assertWithMessage("Character 0x0058 matches caselessly 0x0058")
        .that(scanner.getPropertyValue(88))
        .isEqualTo(88);
    assertWithMessage("Character 0x0059 matches caselessly 0x0059")
        .that(scanner.getPropertyValue(89))
        .isEqualTo(89);
    assertWithMessage("Character 0x005a matches caselessly 0x005a")
        .that(scanner.getPropertyValue(90))
        .isEqualTo(90);
    assertWithMessage("Character 0x0061 matches caselessly 0x0041")
        .that(scanner.getPropertyValue(97))
        .isEqualTo(65);
    assertWithMessage("Character 0x0062 matches caselessly 0x0042")
        .that(scanner.getPropertyValue(98))
        .isEqualTo(66);
    assertWithMessage("Character 0x0063 matches caselessly 0x0043")
        .that(scanner.getPropertyValue(99))
        .isEqualTo(67);
    assertWithMessage("Character 0x0064 matches caselessly 0x0044")
        .that(scanner.getPropertyValue(100))
        .isEqualTo(68);
    assertWithMessage("Character 0x0065 matches caselessly 0x0045")
        .that(scanner.getPropertyValue(101))
        .isEqualTo(69);
    assertWithMessage("Character 0x0066 matches caselessly 0x0046")
        .that(scanner.getPropertyValue(102))
        .isEqualTo(70);
    assertWithMessage("Character 0x0067 matches caselessly 0x0047")
        .that(scanner.getPropertyValue(103))
        .isEqualTo(71);
    assertWithMessage("Character 0x0068 matches caselessly 0x0048")
        .that(scanner.getPropertyValue(104))
        .isEqualTo(72);
    assertWithMessage("Character 0x0069 matches caselessly 0x0049")
        .that(scanner.getPropertyValue(105))
        .isEqualTo(73);
    assertWithMessage("Character 0x006a matches caselessly 0x004a")
        .that(scanner.getPropertyValue(106))
        .isEqualTo(74);
    assertWithMessage("Character 0x006b matches caselessly 0x004b")
        .that(scanner.getPropertyValue(107))
        .isEqualTo(75);
    assertWithMessage("Character 0x006c matches caselessly 0x004c")
        .that(scanner.getPropertyValue(108))
        .isEqualTo(76);
    assertWithMessage("Character 0x006d matches caselessly 0x004d")
        .that(scanner.getPropertyValue(109))
        .isEqualTo(77);
    assertWithMessage("Character 0x006e matches caselessly 0x004e")
        .that(scanner.getPropertyValue(110))
        .isEqualTo(78);
    assertWithMessage("Character 0x006f matches caselessly 0x004f")
        .that(scanner.getPropertyValue(111))
        .isEqualTo(79);
    assertWithMessage("Character 0x0070 matches caselessly 0x0050")
        .that(scanner.getPropertyValue(112))
        .isEqualTo(80);
    assertWithMessage("Character 0x0071 matches caselessly 0x0051")
        .that(scanner.getPropertyValue(113))
        .isEqualTo(81);
    assertWithMessage("Character 0x0072 matches caselessly 0x0052")
        .that(scanner.getPropertyValue(114))
        .isEqualTo(82);
    assertWithMessage("Character 0x0073 matches caselessly 0x0053")
        .that(scanner.getPropertyValue(115))
        .isEqualTo(83);
    assertWithMessage("Character 0x0074 matches caselessly 0x0054")
        .that(scanner.getPropertyValue(116))
        .isEqualTo(84);
    assertWithMessage("Character 0x0075 matches caselessly 0x0055")
        .that(scanner.getPropertyValue(117))
        .isEqualTo(85);
    assertWithMessage("Character 0x0076 matches caselessly 0x0056")
        .that(scanner.getPropertyValue(118))
        .isEqualTo(86);
    assertWithMessage("Character 0x0077 matches caselessly 0x0057")
        .that(scanner.getPropertyValue(119))
        .isEqualTo(87);
    assertWithMessage("Character 0x0078 matches caselessly 0x0058")
        .that(scanner.getPropertyValue(120))
        .isEqualTo(88);
    assertWithMessage("Character 0x0079 matches caselessly 0x0059")
        .that(scanner.getPropertyValue(121))
        .isEqualTo(89);
    assertWithMessage("Character 0x007a matches caselessly 0x005a")
        .that(scanner.getPropertyValue(122))
        .isEqualTo(90);
    assertWithMessage("Character 0x00b5 matches caselessly 0x00b5")
        .that(scanner.getPropertyValue(181))
        .isEqualTo(181);
    assertWithMessage("Character 0x00c0 matches caselessly 0x00c0")
        .that(scanner.getPropertyValue(192))
        .isEqualTo(192);
    assertWithMessage("Character 0x00c1 matches caselessly 0x00c1")
        .that(scanner.getPropertyValue(193))
        .isEqualTo(193);
    assertWithMessage("Character 0x00c2 matches caselessly 0x00c2")
        .that(scanner.getPropertyValue(194))
        .isEqualTo(194);
    assertWithMessage("Character 0x00c3 matches caselessly 0x00c3")
        .that(scanner.getPropertyValue(195))
        .isEqualTo(195);
    assertWithMessage("Character 0x00c4 matches caselessly 0x00c4")
        .that(scanner.getPropertyValue(196))
        .isEqualTo(196);
    assertWithMessage("Character 0x00c5 matches caselessly 0x00c5")
        .that(scanner.getPropertyValue(197))
        .isEqualTo(197);
    assertWithMessage("Character 0x00c6 matches caselessly 0x00c6")
        .that(scanner.getPropertyValue(198))
        .isEqualTo(198);
    assertWithMessage("Character 0x00c7 matches caselessly 0x00c7")
        .that(scanner.getPropertyValue(199))
        .isEqualTo(199);
    assertWithMessage("Character 0x00c8 matches caselessly 0x00c8")
        .that(scanner.getPropertyValue(200))
        .isEqualTo(200);
    assertWithMessage("Character 0x00c9 matches caselessly 0x00c9")
        .that(scanner.getPropertyValue(201))
        .isEqualTo(201);
    assertWithMessage("Character 0x00ca matches caselessly 0x00ca")
        .that(scanner.getPropertyValue(202))
        .isEqualTo(202);
    assertWithMessage("Character 0x00cb matches caselessly 0x00cb")
        .that(scanner.getPropertyValue(203))
        .isEqualTo(203);
    assertWithMessage("Character 0x00cc matches caselessly 0x00cc")
        .that(scanner.getPropertyValue(204))
        .isEqualTo(204);
    assertWithMessage("Character 0x00cd matches caselessly 0x00cd")
        .that(scanner.getPropertyValue(205))
        .isEqualTo(205);
    assertWithMessage("Character 0x00ce matches caselessly 0x00ce")
        .that(scanner.getPropertyValue(206))
        .isEqualTo(206);
    assertWithMessage("Character 0x00cf matches caselessly 0x00cf")
        .that(scanner.getPropertyValue(207))
        .isEqualTo(207);
    assertWithMessage("Character 0x00d0 matches caselessly 0x00d0")
        .that(scanner.getPropertyValue(208))
        .isEqualTo(208);
    assertWithMessage("Character 0x00d1 matches caselessly 0x00d1")
        .that(scanner.getPropertyValue(209))
        .isEqualTo(209);
    assertWithMessage("Character 0x00d2 matches caselessly 0x00d2")
        .that(scanner.getPropertyValue(210))
        .isEqualTo(210);
    assertWithMessage("Character 0x00d3 matches caselessly 0x00d3")
        .that(scanner.getPropertyValue(211))
        .isEqualTo(211);
    assertWithMessage("Character 0x00d4 matches caselessly 0x00d4")
        .that(scanner.getPropertyValue(212))
        .isEqualTo(212);
    assertWithMessage("Character 0x00d5 matches caselessly 0x00d5")
        .that(scanner.getPropertyValue(213))
        .isEqualTo(213);
    assertWithMessage("Character 0x00d6 matches caselessly 0x00d6")
        .that(scanner.getPropertyValue(214))
        .isEqualTo(214);
    assertWithMessage("Character 0x00d8 matches caselessly 0x00d8")
        .that(scanner.getPropertyValue(216))
        .isEqualTo(216);
    assertWithMessage("Character 0x00d9 matches caselessly 0x00d9")
        .that(scanner.getPropertyValue(217))
        .isEqualTo(217);
    assertWithMessage("Character 0x00da matches caselessly 0x00da")
        .that(scanner.getPropertyValue(218))
        .isEqualTo(218);
    assertWithMessage("Character 0x00db matches caselessly 0x00db")
        .that(scanner.getPropertyValue(219))
        .isEqualTo(219);
    assertWithMessage("Character 0x00dc matches caselessly 0x00dc")
        .that(scanner.getPropertyValue(220))
        .isEqualTo(220);
    assertWithMessage("Character 0x00dd matches caselessly 0x00dd")
        .that(scanner.getPropertyValue(221))
        .isEqualTo(221);
    assertWithMessage("Character 0x00de matches caselessly 0x00de")
        .that(scanner.getPropertyValue(222))
        .isEqualTo(222);
    assertWithMessage("Character 0x00e0 matches caselessly 0x00c0")
        .that(scanner.getPropertyValue(224))
        .isEqualTo(192);
    assertWithMessage("Character 0x00e1 matches caselessly 0x00c1")
        .that(scanner.getPropertyValue(225))
        .isEqualTo(193);
    assertWithMessage("Character 0x00e2 matches caselessly 0x00c2")
        .that(scanner.getPropertyValue(226))
        .isEqualTo(194);
    assertWithMessage("Character 0x00e3 matches caselessly 0x00c3")
        .that(scanner.getPropertyValue(227))
        .isEqualTo(195);
    assertWithMessage("Character 0x00e4 matches caselessly 0x00c4")
        .that(scanner.getPropertyValue(228))
        .isEqualTo(196);
    assertWithMessage("Character 0x00e5 matches caselessly 0x00c5")
        .that(scanner.getPropertyValue(229))
        .isEqualTo(197);
    assertWithMessage("Character 0x00e6 matches caselessly 0x00c6")
        .that(scanner.getPropertyValue(230))
        .isEqualTo(198);
    assertWithMessage("Character 0x00e7 matches caselessly 0x00c7")
        .that(scanner.getPropertyValue(231))
        .isEqualTo(199);
    assertWithMessage("Character 0x00e8 matches caselessly 0x00c8")
        .that(scanner.getPropertyValue(232))
        .isEqualTo(200);
    assertWithMessage("Character 0x00e9 matches caselessly 0x00c9")
        .that(scanner.getPropertyValue(233))
        .isEqualTo(201);
    assertWithMessage("Character 0x00ea matches caselessly 0x00ca")
        .that(scanner.getPropertyValue(234))
        .isEqualTo(202);
    assertWithMessage("Character 0x00eb matches caselessly 0x00cb")
        .that(scanner.getPropertyValue(235))
        .isEqualTo(203);
    assertWithMessage("Character 0x00ec matches caselessly 0x00cc")
        .that(scanner.getPropertyValue(236))
        .isEqualTo(204);
    assertWithMessage("Character 0x00ed matches caselessly 0x00cd")
        .that(scanner.getPropertyValue(237))
        .isEqualTo(205);
    assertWithMessage("Character 0x00ee matches caselessly 0x00ce")
        .that(scanner.getPropertyValue(238))
        .isEqualTo(206);
    assertWithMessage("Character 0x00ef matches caselessly 0x00cf")
        .that(scanner.getPropertyValue(239))
        .isEqualTo(207);
    assertWithMessage("Character 0x00f0 matches caselessly 0x00d0")
        .that(scanner.getPropertyValue(240))
        .isEqualTo(208);
    assertWithMessage("Character 0x00f1 matches caselessly 0x00d1")
        .that(scanner.getPropertyValue(241))
        .isEqualTo(209);
    assertWithMessage("Character 0x00f2 matches caselessly 0x00d2")
        .that(scanner.getPropertyValue(242))
        .isEqualTo(210);
    assertWithMessage("Character 0x00f3 matches caselessly 0x00d3")
        .that(scanner.getPropertyValue(243))
        .isEqualTo(211);
    assertWithMessage("Character 0x00f4 matches caselessly 0x00d4")
        .that(scanner.getPropertyValue(244))
        .isEqualTo(212);
    assertWithMessage("Character 0x00f5 matches caselessly 0x00d5")
        .that(scanner.getPropertyValue(245))
        .isEqualTo(213);
    assertWithMessage("Character 0x00f6 matches caselessly 0x00d6")
        .that(scanner.getPropertyValue(246))
        .isEqualTo(214);
    assertWithMessage("Character 0x00f8 matches caselessly 0x00d8")
        .that(scanner.getPropertyValue(248))
        .isEqualTo(216);
    assertWithMessage("Character 0x00f9 matches caselessly 0x00d9")
        .that(scanner.getPropertyValue(249))
        .isEqualTo(217);
    assertWithMessage("Character 0x00fa matches caselessly 0x00da")
        .that(scanner.getPropertyValue(250))
        .isEqualTo(218);
    assertWithMessage("Character 0x00fb matches caselessly 0x00db")
        .that(scanner.getPropertyValue(251))
        .isEqualTo(219);
    assertWithMessage("Character 0x00fc matches caselessly 0x00dc")
        .that(scanner.getPropertyValue(252))
        .isEqualTo(220);
    assertWithMessage("Character 0x00fd matches caselessly 0x00dd")
        .that(scanner.getPropertyValue(253))
        .isEqualTo(221);
    assertWithMessage("Character 0x00fe matches caselessly 0x00de")
        .that(scanner.getPropertyValue(254))
        .isEqualTo(222);
    assertWithMessage("Character 0x00ff matches caselessly 0x00ff")
        .that(scanner.getPropertyValue(255))
        .isEqualTo(255);
    assertWithMessage("Character 0x0100 matches caselessly 0x0100")
        .that(scanner.getPropertyValue(256))
        .isEqualTo(256);
    assertWithMessage("Character 0x0101 matches caselessly 0x0100")
        .that(scanner.getPropertyValue(257))
        .isEqualTo(256);
    assertWithMessage("Character 0x0102 matches caselessly 0x0102")
        .that(scanner.getPropertyValue(258))
        .isEqualTo(258);
    assertWithMessage("Character 0x0103 matches caselessly 0x0102")
        .that(scanner.getPropertyValue(259))
        .isEqualTo(258);
    assertWithMessage("Character 0x0104 matches caselessly 0x0104")
        .that(scanner.getPropertyValue(260))
        .isEqualTo(260);
    assertWithMessage("Character 0x0105 matches caselessly 0x0104")
        .that(scanner.getPropertyValue(261))
        .isEqualTo(260);
    assertWithMessage("Character 0x0106 matches caselessly 0x0106")
        .that(scanner.getPropertyValue(262))
        .isEqualTo(262);
    assertWithMessage("Character 0x0107 matches caselessly 0x0106")
        .that(scanner.getPropertyValue(263))
        .isEqualTo(262);
    assertWithMessage("Character 0x0108 matches caselessly 0x0108")
        .that(scanner.getPropertyValue(264))
        .isEqualTo(264);
    assertWithMessage("Character 0x0109 matches caselessly 0x0108")
        .that(scanner.getPropertyValue(265))
        .isEqualTo(264);
    assertWithMessage("Character 0x010a matches caselessly 0x010a")
        .that(scanner.getPropertyValue(266))
        .isEqualTo(266);
    assertWithMessage("Character 0x010b matches caselessly 0x010a")
        .that(scanner.getPropertyValue(267))
        .isEqualTo(266);
    assertWithMessage("Character 0x010c matches caselessly 0x010c")
        .that(scanner.getPropertyValue(268))
        .isEqualTo(268);
    assertWithMessage("Character 0x010d matches caselessly 0x010c")
        .that(scanner.getPropertyValue(269))
        .isEqualTo(268);
    assertWithMessage("Character 0x010e matches caselessly 0x010e")
        .that(scanner.getPropertyValue(270))
        .isEqualTo(270);
    assertWithMessage("Character 0x010f matches caselessly 0x010e")
        .that(scanner.getPropertyValue(271))
        .isEqualTo(270);
    assertWithMessage("Character 0x0110 matches caselessly 0x0110")
        .that(scanner.getPropertyValue(272))
        .isEqualTo(272);
    assertWithMessage("Character 0x0111 matches caselessly 0x0110")
        .that(scanner.getPropertyValue(273))
        .isEqualTo(272);
    assertWithMessage("Character 0x0112 matches caselessly 0x0112")
        .that(scanner.getPropertyValue(274))
        .isEqualTo(274);
    assertWithMessage("Character 0x0113 matches caselessly 0x0112")
        .that(scanner.getPropertyValue(275))
        .isEqualTo(274);
    assertWithMessage("Character 0x0114 matches caselessly 0x0114")
        .that(scanner.getPropertyValue(276))
        .isEqualTo(276);
    assertWithMessage("Character 0x0115 matches caselessly 0x0114")
        .that(scanner.getPropertyValue(277))
        .isEqualTo(276);
    assertWithMessage("Character 0x0116 matches caselessly 0x0116")
        .that(scanner.getPropertyValue(278))
        .isEqualTo(278);
    assertWithMessage("Character 0x0117 matches caselessly 0x0116")
        .that(scanner.getPropertyValue(279))
        .isEqualTo(278);
    assertWithMessage("Character 0x0118 matches caselessly 0x0118")
        .that(scanner.getPropertyValue(280))
        .isEqualTo(280);
    assertWithMessage("Character 0x0119 matches caselessly 0x0118")
        .that(scanner.getPropertyValue(281))
        .isEqualTo(280);
    assertWithMessage("Character 0x011a matches caselessly 0x011a")
        .that(scanner.getPropertyValue(282))
        .isEqualTo(282);
    assertWithMessage("Character 0x011b matches caselessly 0x011a")
        .that(scanner.getPropertyValue(283))
        .isEqualTo(282);
    assertWithMessage("Character 0x011c matches caselessly 0x011c")
        .that(scanner.getPropertyValue(284))
        .isEqualTo(284);
    assertWithMessage("Character 0x011d matches caselessly 0x011c")
        .that(scanner.getPropertyValue(285))
        .isEqualTo(284);
    assertWithMessage("Character 0x011e matches caselessly 0x011e")
        .that(scanner.getPropertyValue(286))
        .isEqualTo(286);
    assertWithMessage("Character 0x011f matches caselessly 0x011e")
        .that(scanner.getPropertyValue(287))
        .isEqualTo(286);
    assertWithMessage("Character 0x0120 matches caselessly 0x0120")
        .that(scanner.getPropertyValue(288))
        .isEqualTo(288);
    assertWithMessage("Character 0x0121 matches caselessly 0x0120")
        .that(scanner.getPropertyValue(289))
        .isEqualTo(288);
    assertWithMessage("Character 0x0122 matches caselessly 0x0122")
        .that(scanner.getPropertyValue(290))
        .isEqualTo(290);
    assertWithMessage("Character 0x0123 matches caselessly 0x0122")
        .that(scanner.getPropertyValue(291))
        .isEqualTo(290);
    assertWithMessage("Character 0x0124 matches caselessly 0x0124")
        .that(scanner.getPropertyValue(292))
        .isEqualTo(292);
    assertWithMessage("Character 0x0125 matches caselessly 0x0124")
        .that(scanner.getPropertyValue(293))
        .isEqualTo(292);
    assertWithMessage("Character 0x0126 matches caselessly 0x0126")
        .that(scanner.getPropertyValue(294))
        .isEqualTo(294);
    assertWithMessage("Character 0x0127 matches caselessly 0x0126")
        .that(scanner.getPropertyValue(295))
        .isEqualTo(294);
    assertWithMessage("Character 0x0128 matches caselessly 0x0128")
        .that(scanner.getPropertyValue(296))
        .isEqualTo(296);
    assertWithMessage("Character 0x0129 matches caselessly 0x0128")
        .that(scanner.getPropertyValue(297))
        .isEqualTo(296);
    assertWithMessage("Character 0x012a matches caselessly 0x012a")
        .that(scanner.getPropertyValue(298))
        .isEqualTo(298);
    assertWithMessage("Character 0x012b matches caselessly 0x012a")
        .that(scanner.getPropertyValue(299))
        .isEqualTo(298);
    assertWithMessage("Character 0x012c matches caselessly 0x012c")
        .that(scanner.getPropertyValue(300))
        .isEqualTo(300);
    assertWithMessage("Character 0x012d matches caselessly 0x012c")
        .that(scanner.getPropertyValue(301))
        .isEqualTo(300);
    assertWithMessage("Character 0x012e matches caselessly 0x012e")
        .that(scanner.getPropertyValue(302))
        .isEqualTo(302);
    assertWithMessage("Character 0x012f matches caselessly 0x012e")
        .that(scanner.getPropertyValue(303))
        .isEqualTo(302);
    assertWithMessage("Character 0x0130 matches caselessly 0x0049")
        .that(scanner.getPropertyValue(304))
        .isEqualTo(73);
    assertWithMessage("Character 0x0131 matches caselessly 0x0049")
        .that(scanner.getPropertyValue(305))
        .isEqualTo(73);
    assertWithMessage("Character 0x0132 matches caselessly 0x0132")
        .that(scanner.getPropertyValue(306))
        .isEqualTo(306);
    assertWithMessage("Character 0x0133 matches caselessly 0x0132")
        .that(scanner.getPropertyValue(307))
        .isEqualTo(306);
    assertWithMessage("Character 0x0134 matches caselessly 0x0134")
        .that(scanner.getPropertyValue(308))
        .isEqualTo(308);
    assertWithMessage("Character 0x0135 matches caselessly 0x0134")
        .that(scanner.getPropertyValue(309))
        .isEqualTo(308);
    assertWithMessage("Character 0x0136 matches caselessly 0x0136")
        .that(scanner.getPropertyValue(310))
        .isEqualTo(310);
    assertWithMessage("Character 0x0137 matches caselessly 0x0136")
        .that(scanner.getPropertyValue(311))
        .isEqualTo(310);
    assertWithMessage("Character 0x0139 matches caselessly 0x0139")
        .that(scanner.getPropertyValue(313))
        .isEqualTo(313);
    assertWithMessage("Character 0x013a matches caselessly 0x0139")
        .that(scanner.getPropertyValue(314))
        .isEqualTo(313);
    assertWithMessage("Character 0x013b matches caselessly 0x013b")
        .that(scanner.getPropertyValue(315))
        .isEqualTo(315);
    assertWithMessage("Character 0x013c matches caselessly 0x013b")
        .that(scanner.getPropertyValue(316))
        .isEqualTo(315);
    assertWithMessage("Character 0x013d matches caselessly 0x013d")
        .that(scanner.getPropertyValue(317))
        .isEqualTo(317);
    assertWithMessage("Character 0x013e matches caselessly 0x013d")
        .that(scanner.getPropertyValue(318))
        .isEqualTo(317);
    assertWithMessage("Character 0x013f matches caselessly 0x013f")
        .that(scanner.getPropertyValue(319))
        .isEqualTo(319);
    assertWithMessage("Character 0x0140 matches caselessly 0x013f")
        .that(scanner.getPropertyValue(320))
        .isEqualTo(319);
    assertWithMessage("Character 0x0141 matches caselessly 0x0141")
        .that(scanner.getPropertyValue(321))
        .isEqualTo(321);
    assertWithMessage("Character 0x0142 matches caselessly 0x0141")
        .that(scanner.getPropertyValue(322))
        .isEqualTo(321);
    assertWithMessage("Character 0x0143 matches caselessly 0x0143")
        .that(scanner.getPropertyValue(323))
        .isEqualTo(323);
    assertWithMessage("Character 0x0144 matches caselessly 0x0143")
        .that(scanner.getPropertyValue(324))
        .isEqualTo(323);
    assertWithMessage("Character 0x0145 matches caselessly 0x0145")
        .that(scanner.getPropertyValue(325))
        .isEqualTo(325);
    assertWithMessage("Character 0x0146 matches caselessly 0x0145")
        .that(scanner.getPropertyValue(326))
        .isEqualTo(325);
    assertWithMessage("Character 0x0147 matches caselessly 0x0147")
        .that(scanner.getPropertyValue(327))
        .isEqualTo(327);
    assertWithMessage("Character 0x0148 matches caselessly 0x0147")
        .that(scanner.getPropertyValue(328))
        .isEqualTo(327);
    assertWithMessage("Character 0x014a matches caselessly 0x014a")
        .that(scanner.getPropertyValue(330))
        .isEqualTo(330);
    assertWithMessage("Character 0x014b matches caselessly 0x014a")
        .that(scanner.getPropertyValue(331))
        .isEqualTo(330);
    assertWithMessage("Character 0x014c matches caselessly 0x014c")
        .that(scanner.getPropertyValue(332))
        .isEqualTo(332);
    assertWithMessage("Character 0x014d matches caselessly 0x014c")
        .that(scanner.getPropertyValue(333))
        .isEqualTo(332);
    assertWithMessage("Character 0x014e matches caselessly 0x014e")
        .that(scanner.getPropertyValue(334))
        .isEqualTo(334);
    assertWithMessage("Character 0x014f matches caselessly 0x014e")
        .that(scanner.getPropertyValue(335))
        .isEqualTo(334);
    assertWithMessage("Character 0x0150 matches caselessly 0x0150")
        .that(scanner.getPropertyValue(336))
        .isEqualTo(336);
    assertWithMessage("Character 0x0151 matches caselessly 0x0150")
        .that(scanner.getPropertyValue(337))
        .isEqualTo(336);
    assertWithMessage("Character 0x0152 matches caselessly 0x0152")
        .that(scanner.getPropertyValue(338))
        .isEqualTo(338);
    assertWithMessage("Character 0x0153 matches caselessly 0x0152")
        .that(scanner.getPropertyValue(339))
        .isEqualTo(338);
    assertWithMessage("Character 0x0154 matches caselessly 0x0154")
        .that(scanner.getPropertyValue(340))
        .isEqualTo(340);
    assertWithMessage("Character 0x0155 matches caselessly 0x0154")
        .that(scanner.getPropertyValue(341))
        .isEqualTo(340);
    assertWithMessage("Character 0x0156 matches caselessly 0x0156")
        .that(scanner.getPropertyValue(342))
        .isEqualTo(342);
    assertWithMessage("Character 0x0157 matches caselessly 0x0156")
        .that(scanner.getPropertyValue(343))
        .isEqualTo(342);
    assertWithMessage("Character 0x0158 matches caselessly 0x0158")
        .that(scanner.getPropertyValue(344))
        .isEqualTo(344);
    assertWithMessage("Character 0x0159 matches caselessly 0x0158")
        .that(scanner.getPropertyValue(345))
        .isEqualTo(344);
    assertWithMessage("Character 0x015a matches caselessly 0x015a")
        .that(scanner.getPropertyValue(346))
        .isEqualTo(346);
    assertWithMessage("Character 0x015b matches caselessly 0x015a")
        .that(scanner.getPropertyValue(347))
        .isEqualTo(346);
    assertWithMessage("Character 0x015c matches caselessly 0x015c")
        .that(scanner.getPropertyValue(348))
        .isEqualTo(348);
    assertWithMessage("Character 0x015d matches caselessly 0x015c")
        .that(scanner.getPropertyValue(349))
        .isEqualTo(348);
    assertWithMessage("Character 0x015e matches caselessly 0x015e")
        .that(scanner.getPropertyValue(350))
        .isEqualTo(350);
    assertWithMessage("Character 0x015f matches caselessly 0x015e")
        .that(scanner.getPropertyValue(351))
        .isEqualTo(350);
    assertWithMessage("Character 0x0160 matches caselessly 0x0160")
        .that(scanner.getPropertyValue(352))
        .isEqualTo(352);
    assertWithMessage("Character 0x0161 matches caselessly 0x0160")
        .that(scanner.getPropertyValue(353))
        .isEqualTo(352);
    assertWithMessage("Character 0x0162 matches caselessly 0x0162")
        .that(scanner.getPropertyValue(354))
        .isEqualTo(354);
    assertWithMessage("Character 0x0163 matches caselessly 0x0162")
        .that(scanner.getPropertyValue(355))
        .isEqualTo(354);
    assertWithMessage("Character 0x0164 matches caselessly 0x0164")
        .that(scanner.getPropertyValue(356))
        .isEqualTo(356);
    assertWithMessage("Character 0x0165 matches caselessly 0x0164")
        .that(scanner.getPropertyValue(357))
        .isEqualTo(356);
    assertWithMessage("Character 0x0166 matches caselessly 0x0166")
        .that(scanner.getPropertyValue(358))
        .isEqualTo(358);
    assertWithMessage("Character 0x0167 matches caselessly 0x0166")
        .that(scanner.getPropertyValue(359))
        .isEqualTo(358);
    assertWithMessage("Character 0x0168 matches caselessly 0x0168")
        .that(scanner.getPropertyValue(360))
        .isEqualTo(360);
    assertWithMessage("Character 0x0169 matches caselessly 0x0168")
        .that(scanner.getPropertyValue(361))
        .isEqualTo(360);
    assertWithMessage("Character 0x016a matches caselessly 0x016a")
        .that(scanner.getPropertyValue(362))
        .isEqualTo(362);
    assertWithMessage("Character 0x016b matches caselessly 0x016a")
        .that(scanner.getPropertyValue(363))
        .isEqualTo(362);
    assertWithMessage("Character 0x016c matches caselessly 0x016c")
        .that(scanner.getPropertyValue(364))
        .isEqualTo(364);
    assertWithMessage("Character 0x016d matches caselessly 0x016c")
        .that(scanner.getPropertyValue(365))
        .isEqualTo(364);
    assertWithMessage("Character 0x016e matches caselessly 0x016e")
        .that(scanner.getPropertyValue(366))
        .isEqualTo(366);
    assertWithMessage("Character 0x016f matches caselessly 0x016e")
        .that(scanner.getPropertyValue(367))
        .isEqualTo(366);
    assertWithMessage("Character 0x0170 matches caselessly 0x0170")
        .that(scanner.getPropertyValue(368))
        .isEqualTo(368);
    assertWithMessage("Character 0x0171 matches caselessly 0x0170")
        .that(scanner.getPropertyValue(369))
        .isEqualTo(368);
    assertWithMessage("Character 0x0172 matches caselessly 0x0172")
        .that(scanner.getPropertyValue(370))
        .isEqualTo(370);
    assertWithMessage("Character 0x0173 matches caselessly 0x0172")
        .that(scanner.getPropertyValue(371))
        .isEqualTo(370);
    assertWithMessage("Character 0x0174 matches caselessly 0x0174")
        .that(scanner.getPropertyValue(372))
        .isEqualTo(372);
    assertWithMessage("Character 0x0175 matches caselessly 0x0174")
        .that(scanner.getPropertyValue(373))
        .isEqualTo(372);
    assertWithMessage("Character 0x0176 matches caselessly 0x0176")
        .that(scanner.getPropertyValue(374))
        .isEqualTo(374);
    assertWithMessage("Character 0x0177 matches caselessly 0x0176")
        .that(scanner.getPropertyValue(375))
        .isEqualTo(374);
    assertWithMessage("Character 0x0178 matches caselessly 0x00ff")
        .that(scanner.getPropertyValue(376))
        .isEqualTo(255);
    assertWithMessage("Character 0x0179 matches caselessly 0x0179")
        .that(scanner.getPropertyValue(377))
        .isEqualTo(377);
    assertWithMessage("Character 0x017a matches caselessly 0x0179")
        .that(scanner.getPropertyValue(378))
        .isEqualTo(377);
    assertWithMessage("Character 0x017b matches caselessly 0x017b")
        .that(scanner.getPropertyValue(379))
        .isEqualTo(379);
    assertWithMessage("Character 0x017c matches caselessly 0x017b")
        .that(scanner.getPropertyValue(380))
        .isEqualTo(379);
    assertWithMessage("Character 0x017d matches caselessly 0x017d")
        .that(scanner.getPropertyValue(381))
        .isEqualTo(381);
    assertWithMessage("Character 0x017e matches caselessly 0x017d")
        .that(scanner.getPropertyValue(382))
        .isEqualTo(381);
    assertWithMessage("Character 0x017f matches caselessly 0x0053")
        .that(scanner.getPropertyValue(383))
        .isEqualTo(83);
    assertWithMessage("Character 0x0181 matches caselessly 0x0181")
        .that(scanner.getPropertyValue(385))
        .isEqualTo(385);
    assertWithMessage("Character 0x0182 matches caselessly 0x0182")
        .that(scanner.getPropertyValue(386))
        .isEqualTo(386);
    assertWithMessage("Character 0x0183 matches caselessly 0x0182")
        .that(scanner.getPropertyValue(387))
        .isEqualTo(386);
    assertWithMessage("Character 0x0184 matches caselessly 0x0184")
        .that(scanner.getPropertyValue(388))
        .isEqualTo(388);
    assertWithMessage("Character 0x0185 matches caselessly 0x0184")
        .that(scanner.getPropertyValue(389))
        .isEqualTo(388);
    assertWithMessage("Character 0x0186 matches caselessly 0x0186")
        .that(scanner.getPropertyValue(390))
        .isEqualTo(390);
    assertWithMessage("Character 0x0187 matches caselessly 0x0187")
        .that(scanner.getPropertyValue(391))
        .isEqualTo(391);
    assertWithMessage("Character 0x0188 matches caselessly 0x0187")
        .that(scanner.getPropertyValue(392))
        .isEqualTo(391);
    assertWithMessage("Character 0x0189 matches caselessly 0x0189")
        .that(scanner.getPropertyValue(393))
        .isEqualTo(393);
    assertWithMessage("Character 0x018a matches caselessly 0x018a")
        .that(scanner.getPropertyValue(394))
        .isEqualTo(394);
    assertWithMessage("Character 0x018b matches caselessly 0x018b")
        .that(scanner.getPropertyValue(395))
        .isEqualTo(395);
    assertWithMessage("Character 0x018c matches caselessly 0x018b")
        .that(scanner.getPropertyValue(396))
        .isEqualTo(395);
    assertWithMessage("Character 0x018e matches caselessly 0x018e")
        .that(scanner.getPropertyValue(398))
        .isEqualTo(398);
    assertWithMessage("Character 0x018f matches caselessly 0x018f")
        .that(scanner.getPropertyValue(399))
        .isEqualTo(399);
    assertWithMessage("Character 0x0190 matches caselessly 0x0190")
        .that(scanner.getPropertyValue(400))
        .isEqualTo(400);
    assertWithMessage("Character 0x0191 matches caselessly 0x0191")
        .that(scanner.getPropertyValue(401))
        .isEqualTo(401);
    assertWithMessage("Character 0x0192 matches caselessly 0x0191")
        .that(scanner.getPropertyValue(402))
        .isEqualTo(401);
    assertWithMessage("Character 0x0193 matches caselessly 0x0193")
        .that(scanner.getPropertyValue(403))
        .isEqualTo(403);
    assertWithMessage("Character 0x0194 matches caselessly 0x0194")
        .that(scanner.getPropertyValue(404))
        .isEqualTo(404);
    assertWithMessage("Character 0x0195 matches caselessly 0x0195")
        .that(scanner.getPropertyValue(405))
        .isEqualTo(405);
    assertWithMessage("Character 0x0196 matches caselessly 0x0196")
        .that(scanner.getPropertyValue(406))
        .isEqualTo(406);
    assertWithMessage("Character 0x0197 matches caselessly 0x0197")
        .that(scanner.getPropertyValue(407))
        .isEqualTo(407);
    assertWithMessage("Character 0x0198 matches caselessly 0x0198")
        .that(scanner.getPropertyValue(408))
        .isEqualTo(408);
    assertWithMessage("Character 0x0199 matches caselessly 0x0198")
        .that(scanner.getPropertyValue(409))
        .isEqualTo(408);
    assertWithMessage("Character 0x019a matches caselessly 0x019a")
        .that(scanner.getPropertyValue(410))
        .isEqualTo(410);
    assertWithMessage("Character 0x019c matches caselessly 0x019c")
        .that(scanner.getPropertyValue(412))
        .isEqualTo(412);
    assertWithMessage("Character 0x019d matches caselessly 0x019d")
        .that(scanner.getPropertyValue(413))
        .isEqualTo(413);
    assertWithMessage("Character 0x019e matches caselessly 0x019e")
        .that(scanner.getPropertyValue(414))
        .isEqualTo(414);
    assertWithMessage("Character 0x019f matches caselessly 0x019f")
        .that(scanner.getPropertyValue(415))
        .isEqualTo(415);
    assertWithMessage("Character 0x01a0 matches caselessly 0x01a0")
        .that(scanner.getPropertyValue(416))
        .isEqualTo(416);
    assertWithMessage("Character 0x01a1 matches caselessly 0x01a0")
        .that(scanner.getPropertyValue(417))
        .isEqualTo(416);
    assertWithMessage("Character 0x01a2 matches caselessly 0x01a2")
        .that(scanner.getPropertyValue(418))
        .isEqualTo(418);
    assertWithMessage("Character 0x01a3 matches caselessly 0x01a2")
        .that(scanner.getPropertyValue(419))
        .isEqualTo(418);
    assertWithMessage("Character 0x01a4 matches caselessly 0x01a4")
        .that(scanner.getPropertyValue(420))
        .isEqualTo(420);
    assertWithMessage("Character 0x01a5 matches caselessly 0x01a4")
        .that(scanner.getPropertyValue(421))
        .isEqualTo(420);
    assertWithMessage("Character 0x01a6 matches caselessly 0x01a6")
        .that(scanner.getPropertyValue(422))
        .isEqualTo(422);
    assertWithMessage("Character 0x01a7 matches caselessly 0x01a7")
        .that(scanner.getPropertyValue(423))
        .isEqualTo(423);
    assertWithMessage("Character 0x01a8 matches caselessly 0x01a7")
        .that(scanner.getPropertyValue(424))
        .isEqualTo(423);
    assertWithMessage("Character 0x01a9 matches caselessly 0x01a9")
        .that(scanner.getPropertyValue(425))
        .isEqualTo(425);
    assertWithMessage("Character 0x01ac matches caselessly 0x01ac")
        .that(scanner.getPropertyValue(428))
        .isEqualTo(428);
    assertWithMessage("Character 0x01ad matches caselessly 0x01ac")
        .that(scanner.getPropertyValue(429))
        .isEqualTo(428);
    assertWithMessage("Character 0x01ae matches caselessly 0x01ae")
        .that(scanner.getPropertyValue(430))
        .isEqualTo(430);
    assertWithMessage("Character 0x01af matches caselessly 0x01af")
        .that(scanner.getPropertyValue(431))
        .isEqualTo(431);
    assertWithMessage("Character 0x01b0 matches caselessly 0x01af")
        .that(scanner.getPropertyValue(432))
        .isEqualTo(431);
    assertWithMessage("Character 0x01b1 matches caselessly 0x01b1")
        .that(scanner.getPropertyValue(433))
        .isEqualTo(433);
    assertWithMessage("Character 0x01b2 matches caselessly 0x01b2")
        .that(scanner.getPropertyValue(434))
        .isEqualTo(434);
    assertWithMessage("Character 0x01b3 matches caselessly 0x01b3")
        .that(scanner.getPropertyValue(435))
        .isEqualTo(435);
    assertWithMessage("Character 0x01b4 matches caselessly 0x01b3")
        .that(scanner.getPropertyValue(436))
        .isEqualTo(435);
    assertWithMessage("Character 0x01b5 matches caselessly 0x01b5")
        .that(scanner.getPropertyValue(437))
        .isEqualTo(437);
    assertWithMessage("Character 0x01b6 matches caselessly 0x01b5")
        .that(scanner.getPropertyValue(438))
        .isEqualTo(437);
    assertWithMessage("Character 0x01b7 matches caselessly 0x01b7")
        .that(scanner.getPropertyValue(439))
        .isEqualTo(439);
    assertWithMessage("Character 0x01b8 matches caselessly 0x01b8")
        .that(scanner.getPropertyValue(440))
        .isEqualTo(440);
    assertWithMessage("Character 0x01b9 matches caselessly 0x01b8")
        .that(scanner.getPropertyValue(441))
        .isEqualTo(440);
    assertWithMessage("Character 0x01bc matches caselessly 0x01bc")
        .that(scanner.getPropertyValue(444))
        .isEqualTo(444);
    assertWithMessage("Character 0x01bd matches caselessly 0x01bc")
        .that(scanner.getPropertyValue(445))
        .isEqualTo(444);
    assertWithMessage("Character 0x01bf matches caselessly 0x01bf")
        .that(scanner.getPropertyValue(447))
        .isEqualTo(447);
    assertWithMessage("Character 0x01c4 matches caselessly 0x01c4")
        .that(scanner.getPropertyValue(452))
        .isEqualTo(452);
    assertWithMessage("Character 0x01c5 matches caselessly 0x01c4")
        .that(scanner.getPropertyValue(453))
        .isEqualTo(452);
    assertWithMessage("Character 0x01c6 matches caselessly 0x01c4")
        .that(scanner.getPropertyValue(454))
        .isEqualTo(452);
    assertWithMessage("Character 0x01c7 matches caselessly 0x01c7")
        .that(scanner.getPropertyValue(455))
        .isEqualTo(455);
    assertWithMessage("Character 0x01c8 matches caselessly 0x01c7")
        .that(scanner.getPropertyValue(456))
        .isEqualTo(455);
    assertWithMessage("Character 0x01c9 matches caselessly 0x01c7")
        .that(scanner.getPropertyValue(457))
        .isEqualTo(455);
    assertWithMessage("Character 0x01ca matches caselessly 0x01ca")
        .that(scanner.getPropertyValue(458))
        .isEqualTo(458);
    assertWithMessage("Character 0x01cb matches caselessly 0x01ca")
        .that(scanner.getPropertyValue(459))
        .isEqualTo(458);
    assertWithMessage("Character 0x01cc matches caselessly 0x01ca")
        .that(scanner.getPropertyValue(460))
        .isEqualTo(458);
    assertWithMessage("Character 0x01cd matches caselessly 0x01cd")
        .that(scanner.getPropertyValue(461))
        .isEqualTo(461);
    assertWithMessage("Character 0x01ce matches caselessly 0x01cd")
        .that(scanner.getPropertyValue(462))
        .isEqualTo(461);
    assertWithMessage("Character 0x01cf matches caselessly 0x01cf")
        .that(scanner.getPropertyValue(463))
        .isEqualTo(463);
    assertWithMessage("Character 0x01d0 matches caselessly 0x01cf")
        .that(scanner.getPropertyValue(464))
        .isEqualTo(463);
    assertWithMessage("Character 0x01d1 matches caselessly 0x01d1")
        .that(scanner.getPropertyValue(465))
        .isEqualTo(465);
    assertWithMessage("Character 0x01d2 matches caselessly 0x01d1")
        .that(scanner.getPropertyValue(466))
        .isEqualTo(465);
    assertWithMessage("Character 0x01d3 matches caselessly 0x01d3")
        .that(scanner.getPropertyValue(467))
        .isEqualTo(467);
    assertWithMessage("Character 0x01d4 matches caselessly 0x01d3")
        .that(scanner.getPropertyValue(468))
        .isEqualTo(467);
    assertWithMessage("Character 0x01d5 matches caselessly 0x01d5")
        .that(scanner.getPropertyValue(469))
        .isEqualTo(469);
    assertWithMessage("Character 0x01d6 matches caselessly 0x01d5")
        .that(scanner.getPropertyValue(470))
        .isEqualTo(469);
    assertWithMessage("Character 0x01d7 matches caselessly 0x01d7")
        .that(scanner.getPropertyValue(471))
        .isEqualTo(471);
    assertWithMessage("Character 0x01d8 matches caselessly 0x01d7")
        .that(scanner.getPropertyValue(472))
        .isEqualTo(471);
    assertWithMessage("Character 0x01d9 matches caselessly 0x01d9")
        .that(scanner.getPropertyValue(473))
        .isEqualTo(473);
    assertWithMessage("Character 0x01da matches caselessly 0x01d9")
        .that(scanner.getPropertyValue(474))
        .isEqualTo(473);
    assertWithMessage("Character 0x01db matches caselessly 0x01db")
        .that(scanner.getPropertyValue(475))
        .isEqualTo(475);
    assertWithMessage("Character 0x01dc matches caselessly 0x01db")
        .that(scanner.getPropertyValue(476))
        .isEqualTo(475);
    assertWithMessage("Character 0x01dd matches caselessly 0x018e")
        .that(scanner.getPropertyValue(477))
        .isEqualTo(398);
    assertWithMessage("Character 0x01de matches caselessly 0x01de")
        .that(scanner.getPropertyValue(478))
        .isEqualTo(478);
    assertWithMessage("Character 0x01df matches caselessly 0x01de")
        .that(scanner.getPropertyValue(479))
        .isEqualTo(478);
    assertWithMessage("Character 0x01e0 matches caselessly 0x01e0")
        .that(scanner.getPropertyValue(480))
        .isEqualTo(480);
    assertWithMessage("Character 0x01e1 matches caselessly 0x01e0")
        .that(scanner.getPropertyValue(481))
        .isEqualTo(480);
    assertWithMessage("Character 0x01e2 matches caselessly 0x01e2")
        .that(scanner.getPropertyValue(482))
        .isEqualTo(482);
    assertWithMessage("Character 0x01e3 matches caselessly 0x01e2")
        .that(scanner.getPropertyValue(483))
        .isEqualTo(482);
    assertWithMessage("Character 0x01e4 matches caselessly 0x01e4")
        .that(scanner.getPropertyValue(484))
        .isEqualTo(484);
    assertWithMessage("Character 0x01e5 matches caselessly 0x01e4")
        .that(scanner.getPropertyValue(485))
        .isEqualTo(484);
    assertWithMessage("Character 0x01e6 matches caselessly 0x01e6")
        .that(scanner.getPropertyValue(486))
        .isEqualTo(486);
    assertWithMessage("Character 0x01e7 matches caselessly 0x01e6")
        .that(scanner.getPropertyValue(487))
        .isEqualTo(486);
    assertWithMessage("Character 0x01e8 matches caselessly 0x01e8")
        .that(scanner.getPropertyValue(488))
        .isEqualTo(488);
    assertWithMessage("Character 0x01e9 matches caselessly 0x01e8")
        .that(scanner.getPropertyValue(489))
        .isEqualTo(488);
    assertWithMessage("Character 0x01ea matches caselessly 0x01ea")
        .that(scanner.getPropertyValue(490))
        .isEqualTo(490);
    assertWithMessage("Character 0x01eb matches caselessly 0x01ea")
        .that(scanner.getPropertyValue(491))
        .isEqualTo(490);
    assertWithMessage("Character 0x01ec matches caselessly 0x01ec")
        .that(scanner.getPropertyValue(492))
        .isEqualTo(492);
    assertWithMessage("Character 0x01ed matches caselessly 0x01ec")
        .that(scanner.getPropertyValue(493))
        .isEqualTo(492);
    assertWithMessage("Character 0x01ee matches caselessly 0x01ee")
        .that(scanner.getPropertyValue(494))
        .isEqualTo(494);
    assertWithMessage("Character 0x01ef matches caselessly 0x01ee")
        .that(scanner.getPropertyValue(495))
        .isEqualTo(494);
    assertWithMessage("Character 0x01f1 matches caselessly 0x01f1")
        .that(scanner.getPropertyValue(497))
        .isEqualTo(497);
    assertWithMessage("Character 0x01f2 matches caselessly 0x01f1")
        .that(scanner.getPropertyValue(498))
        .isEqualTo(497);
    assertWithMessage("Character 0x01f3 matches caselessly 0x01f1")
        .that(scanner.getPropertyValue(499))
        .isEqualTo(497);
    assertWithMessage("Character 0x01f4 matches caselessly 0x01f4")
        .that(scanner.getPropertyValue(500))
        .isEqualTo(500);
    assertWithMessage("Character 0x01f5 matches caselessly 0x01f4")
        .that(scanner.getPropertyValue(501))
        .isEqualTo(500);
    assertWithMessage("Character 0x01f6 matches caselessly 0x0195")
        .that(scanner.getPropertyValue(502))
        .isEqualTo(405);
    assertWithMessage("Character 0x01f7 matches caselessly 0x01bf")
        .that(scanner.getPropertyValue(503))
        .isEqualTo(447);
    assertWithMessage("Character 0x01f8 matches caselessly 0x01f8")
        .that(scanner.getPropertyValue(504))
        .isEqualTo(504);
    assertWithMessage("Character 0x01f9 matches caselessly 0x01f8")
        .that(scanner.getPropertyValue(505))
        .isEqualTo(504);
    assertWithMessage("Character 0x01fa matches caselessly 0x01fa")
        .that(scanner.getPropertyValue(506))
        .isEqualTo(506);
    assertWithMessage("Character 0x01fb matches caselessly 0x01fa")
        .that(scanner.getPropertyValue(507))
        .isEqualTo(506);
    assertWithMessage("Character 0x01fc matches caselessly 0x01fc")
        .that(scanner.getPropertyValue(508))
        .isEqualTo(508);
    assertWithMessage("Character 0x01fd matches caselessly 0x01fc")
        .that(scanner.getPropertyValue(509))
        .isEqualTo(508);
    assertWithMessage("Character 0x01fe matches caselessly 0x01fe")
        .that(scanner.getPropertyValue(510))
        .isEqualTo(510);
    assertWithMessage("Character 0x01ff matches caselessly 0x01fe")
        .that(scanner.getPropertyValue(511))
        .isEqualTo(510);
    assertWithMessage("Character 0x0200 matches caselessly 0x0200")
        .that(scanner.getPropertyValue(512))
        .isEqualTo(512);
    assertWithMessage("Character 0x0201 matches caselessly 0x0200")
        .that(scanner.getPropertyValue(513))
        .isEqualTo(512);
    assertWithMessage("Character 0x0202 matches caselessly 0x0202")
        .that(scanner.getPropertyValue(514))
        .isEqualTo(514);
    assertWithMessage("Character 0x0203 matches caselessly 0x0202")
        .that(scanner.getPropertyValue(515))
        .isEqualTo(514);
    assertWithMessage("Character 0x0204 matches caselessly 0x0204")
        .that(scanner.getPropertyValue(516))
        .isEqualTo(516);
    assertWithMessage("Character 0x0205 matches caselessly 0x0204")
        .that(scanner.getPropertyValue(517))
        .isEqualTo(516);
    assertWithMessage("Character 0x0206 matches caselessly 0x0206")
        .that(scanner.getPropertyValue(518))
        .isEqualTo(518);
    assertWithMessage("Character 0x0207 matches caselessly 0x0206")
        .that(scanner.getPropertyValue(519))
        .isEqualTo(518);
    assertWithMessage("Character 0x0208 matches caselessly 0x0208")
        .that(scanner.getPropertyValue(520))
        .isEqualTo(520);
    assertWithMessage("Character 0x0209 matches caselessly 0x0208")
        .that(scanner.getPropertyValue(521))
        .isEqualTo(520);
    assertWithMessage("Character 0x020a matches caselessly 0x020a")
        .that(scanner.getPropertyValue(522))
        .isEqualTo(522);
    assertWithMessage("Character 0x020b matches caselessly 0x020a")
        .that(scanner.getPropertyValue(523))
        .isEqualTo(522);
    assertWithMessage("Character 0x020c matches caselessly 0x020c")
        .that(scanner.getPropertyValue(524))
        .isEqualTo(524);
    assertWithMessage("Character 0x020d matches caselessly 0x020c")
        .that(scanner.getPropertyValue(525))
        .isEqualTo(524);
    assertWithMessage("Character 0x020e matches caselessly 0x020e")
        .that(scanner.getPropertyValue(526))
        .isEqualTo(526);
    assertWithMessage("Character 0x020f matches caselessly 0x020e")
        .that(scanner.getPropertyValue(527))
        .isEqualTo(526);
    assertWithMessage("Character 0x0210 matches caselessly 0x0210")
        .that(scanner.getPropertyValue(528))
        .isEqualTo(528);
    assertWithMessage("Character 0x0211 matches caselessly 0x0210")
        .that(scanner.getPropertyValue(529))
        .isEqualTo(528);
    assertWithMessage("Character 0x0212 matches caselessly 0x0212")
        .that(scanner.getPropertyValue(530))
        .isEqualTo(530);
    assertWithMessage("Character 0x0213 matches caselessly 0x0212")
        .that(scanner.getPropertyValue(531))
        .isEqualTo(530);
    assertWithMessage("Character 0x0214 matches caselessly 0x0214")
        .that(scanner.getPropertyValue(532))
        .isEqualTo(532);
    assertWithMessage("Character 0x0215 matches caselessly 0x0214")
        .that(scanner.getPropertyValue(533))
        .isEqualTo(532);
    assertWithMessage("Character 0x0216 matches caselessly 0x0216")
        .that(scanner.getPropertyValue(534))
        .isEqualTo(534);
    assertWithMessage("Character 0x0217 matches caselessly 0x0216")
        .that(scanner.getPropertyValue(535))
        .isEqualTo(534);
    assertWithMessage("Character 0x0218 matches caselessly 0x0218")
        .that(scanner.getPropertyValue(536))
        .isEqualTo(536);
    assertWithMessage("Character 0x0219 matches caselessly 0x0218")
        .that(scanner.getPropertyValue(537))
        .isEqualTo(536);
    assertWithMessage("Character 0x021a matches caselessly 0x021a")
        .that(scanner.getPropertyValue(538))
        .isEqualTo(538);
    assertWithMessage("Character 0x021b matches caselessly 0x021a")
        .that(scanner.getPropertyValue(539))
        .isEqualTo(538);
    assertWithMessage("Character 0x021c matches caselessly 0x021c")
        .that(scanner.getPropertyValue(540))
        .isEqualTo(540);
    assertWithMessage("Character 0x021d matches caselessly 0x021c")
        .that(scanner.getPropertyValue(541))
        .isEqualTo(540);
    assertWithMessage("Character 0x021e matches caselessly 0x021e")
        .that(scanner.getPropertyValue(542))
        .isEqualTo(542);
    assertWithMessage("Character 0x021f matches caselessly 0x021e")
        .that(scanner.getPropertyValue(543))
        .isEqualTo(542);
    assertWithMessage("Character 0x0220 matches caselessly 0x019e")
        .that(scanner.getPropertyValue(544))
        .isEqualTo(414);
    assertWithMessage("Character 0x0222 matches caselessly 0x0222")
        .that(scanner.getPropertyValue(546))
        .isEqualTo(546);
    assertWithMessage("Character 0x0223 matches caselessly 0x0222")
        .that(scanner.getPropertyValue(547))
        .isEqualTo(546);
    assertWithMessage("Character 0x0224 matches caselessly 0x0224")
        .that(scanner.getPropertyValue(548))
        .isEqualTo(548);
    assertWithMessage("Character 0x0225 matches caselessly 0x0224")
        .that(scanner.getPropertyValue(549))
        .isEqualTo(548);
    assertWithMessage("Character 0x0226 matches caselessly 0x0226")
        .that(scanner.getPropertyValue(550))
        .isEqualTo(550);
    assertWithMessage("Character 0x0227 matches caselessly 0x0226")
        .that(scanner.getPropertyValue(551))
        .isEqualTo(550);
    assertWithMessage("Character 0x0228 matches caselessly 0x0228")
        .that(scanner.getPropertyValue(552))
        .isEqualTo(552);
    assertWithMessage("Character 0x0229 matches caselessly 0x0228")
        .that(scanner.getPropertyValue(553))
        .isEqualTo(552);
    assertWithMessage("Character 0x022a matches caselessly 0x022a")
        .that(scanner.getPropertyValue(554))
        .isEqualTo(554);
    assertWithMessage("Character 0x022b matches caselessly 0x022a")
        .that(scanner.getPropertyValue(555))
        .isEqualTo(554);
    assertWithMessage("Character 0x022c matches caselessly 0x022c")
        .that(scanner.getPropertyValue(556))
        .isEqualTo(556);
    assertWithMessage("Character 0x022d matches caselessly 0x022c")
        .that(scanner.getPropertyValue(557))
        .isEqualTo(556);
    assertWithMessage("Character 0x022e matches caselessly 0x022e")
        .that(scanner.getPropertyValue(558))
        .isEqualTo(558);
    assertWithMessage("Character 0x022f matches caselessly 0x022e")
        .that(scanner.getPropertyValue(559))
        .isEqualTo(558);
    assertWithMessage("Character 0x0230 matches caselessly 0x0230")
        .that(scanner.getPropertyValue(560))
        .isEqualTo(560);
    assertWithMessage("Character 0x0231 matches caselessly 0x0230")
        .that(scanner.getPropertyValue(561))
        .isEqualTo(560);
    assertWithMessage("Character 0x0232 matches caselessly 0x0232")
        .that(scanner.getPropertyValue(562))
        .isEqualTo(562);
    assertWithMessage("Character 0x0233 matches caselessly 0x0232")
        .that(scanner.getPropertyValue(563))
        .isEqualTo(562);
    assertWithMessage("Character 0x023b matches caselessly 0x023b")
        .that(scanner.getPropertyValue(571))
        .isEqualTo(571);
    assertWithMessage("Character 0x023c matches caselessly 0x023b")
        .that(scanner.getPropertyValue(572))
        .isEqualTo(571);
    assertWithMessage("Character 0x023d matches caselessly 0x019a")
        .that(scanner.getPropertyValue(573))
        .isEqualTo(410);
    assertWithMessage("Character 0x0241 matches caselessly 0x0241")
        .that(scanner.getPropertyValue(577))
        .isEqualTo(577);
    assertWithMessage("Character 0x0253 matches caselessly 0x0181")
        .that(scanner.getPropertyValue(595))
        .isEqualTo(385);
    assertWithMessage("Character 0x0254 matches caselessly 0x0186")
        .that(scanner.getPropertyValue(596))
        .isEqualTo(390);
    assertWithMessage("Character 0x0256 matches caselessly 0x0189")
        .that(scanner.getPropertyValue(598))
        .isEqualTo(393);
    assertWithMessage("Character 0x0257 matches caselessly 0x018a")
        .that(scanner.getPropertyValue(599))
        .isEqualTo(394);
    assertWithMessage("Character 0x0259 matches caselessly 0x018f")
        .that(scanner.getPropertyValue(601))
        .isEqualTo(399);
    assertWithMessage("Character 0x025b matches caselessly 0x0190")
        .that(scanner.getPropertyValue(603))
        .isEqualTo(400);
    assertWithMessage("Character 0x0260 matches caselessly 0x0193")
        .that(scanner.getPropertyValue(608))
        .isEqualTo(403);
    assertWithMessage("Character 0x0263 matches caselessly 0x0194")
        .that(scanner.getPropertyValue(611))
        .isEqualTo(404);
    assertWithMessage("Character 0x0268 matches caselessly 0x0197")
        .that(scanner.getPropertyValue(616))
        .isEqualTo(407);
    assertWithMessage("Character 0x0269 matches caselessly 0x0196")
        .that(scanner.getPropertyValue(617))
        .isEqualTo(406);
    assertWithMessage("Character 0x026f matches caselessly 0x019c")
        .that(scanner.getPropertyValue(623))
        .isEqualTo(412);
    assertWithMessage("Character 0x0272 matches caselessly 0x019d")
        .that(scanner.getPropertyValue(626))
        .isEqualTo(413);
    assertWithMessage("Character 0x0275 matches caselessly 0x019f")
        .that(scanner.getPropertyValue(629))
        .isEqualTo(415);
    assertWithMessage("Character 0x0280 matches caselessly 0x01a6")
        .that(scanner.getPropertyValue(640))
        .isEqualTo(422);
    assertWithMessage("Character 0x0283 matches caselessly 0x01a9")
        .that(scanner.getPropertyValue(643))
        .isEqualTo(425);
    assertWithMessage("Character 0x0288 matches caselessly 0x01ae")
        .that(scanner.getPropertyValue(648))
        .isEqualTo(430);
    assertWithMessage("Character 0x028a matches caselessly 0x01b1")
        .that(scanner.getPropertyValue(650))
        .isEqualTo(433);
    assertWithMessage("Character 0x028b matches caselessly 0x01b2")
        .that(scanner.getPropertyValue(651))
        .isEqualTo(434);
    assertWithMessage("Character 0x0292 matches caselessly 0x01b7")
        .that(scanner.getPropertyValue(658))
        .isEqualTo(439);
    assertWithMessage("Character 0x0294 matches caselessly 0x0241")
        .that(scanner.getPropertyValue(660))
        .isEqualTo(577);
    assertWithMessage("Character 0x0345 matches caselessly 0x0345")
        .that(scanner.getPropertyValue(837))
        .isEqualTo(837);
    assertWithMessage("Character 0x0386 matches caselessly 0x0386")
        .that(scanner.getPropertyValue(902))
        .isEqualTo(902);
    assertWithMessage("Character 0x0388 matches caselessly 0x0388")
        .that(scanner.getPropertyValue(904))
        .isEqualTo(904);
    assertWithMessage("Character 0x0389 matches caselessly 0x0389")
        .that(scanner.getPropertyValue(905))
        .isEqualTo(905);
    assertWithMessage("Character 0x038a matches caselessly 0x038a")
        .that(scanner.getPropertyValue(906))
        .isEqualTo(906);
    assertWithMessage("Character 0x038c matches caselessly 0x038c")
        .that(scanner.getPropertyValue(908))
        .isEqualTo(908);
    assertWithMessage("Character 0x038e matches caselessly 0x038e")
        .that(scanner.getPropertyValue(910))
        .isEqualTo(910);
    assertWithMessage("Character 0x038f matches caselessly 0x038f")
        .that(scanner.getPropertyValue(911))
        .isEqualTo(911);
    assertWithMessage("Character 0x0391 matches caselessly 0x0391")
        .that(scanner.getPropertyValue(913))
        .isEqualTo(913);
    assertWithMessage("Character 0x0392 matches caselessly 0x0392")
        .that(scanner.getPropertyValue(914))
        .isEqualTo(914);
    assertWithMessage("Character 0x0393 matches caselessly 0x0393")
        .that(scanner.getPropertyValue(915))
        .isEqualTo(915);
    assertWithMessage("Character 0x0394 matches caselessly 0x0394")
        .that(scanner.getPropertyValue(916))
        .isEqualTo(916);
    assertWithMessage("Character 0x0395 matches caselessly 0x0395")
        .that(scanner.getPropertyValue(917))
        .isEqualTo(917);
    assertWithMessage("Character 0x0396 matches caselessly 0x0396")
        .that(scanner.getPropertyValue(918))
        .isEqualTo(918);
    assertWithMessage("Character 0x0397 matches caselessly 0x0397")
        .that(scanner.getPropertyValue(919))
        .isEqualTo(919);
    assertWithMessage("Character 0x0398 matches caselessly 0x0398")
        .that(scanner.getPropertyValue(920))
        .isEqualTo(920);
    assertWithMessage("Character 0x0399 matches caselessly 0x0345")
        .that(scanner.getPropertyValue(921))
        .isEqualTo(837);
    assertWithMessage("Character 0x039a matches caselessly 0x039a")
        .that(scanner.getPropertyValue(922))
        .isEqualTo(922);
    assertWithMessage("Character 0x039b matches caselessly 0x039b")
        .that(scanner.getPropertyValue(923))
        .isEqualTo(923);
    assertWithMessage("Character 0x039c matches caselessly 0x00b5")
        .that(scanner.getPropertyValue(924))
        .isEqualTo(181);
    assertWithMessage("Character 0x039d matches caselessly 0x039d")
        .that(scanner.getPropertyValue(925))
        .isEqualTo(925);
    assertWithMessage("Character 0x039e matches caselessly 0x039e")
        .that(scanner.getPropertyValue(926))
        .isEqualTo(926);
    assertWithMessage("Character 0x039f matches caselessly 0x039f")
        .that(scanner.getPropertyValue(927))
        .isEqualTo(927);
    assertWithMessage("Character 0x03a0 matches caselessly 0x03a0")
        .that(scanner.getPropertyValue(928))
        .isEqualTo(928);
    assertWithMessage("Character 0x03a1 matches caselessly 0x03a1")
        .that(scanner.getPropertyValue(929))
        .isEqualTo(929);
    assertWithMessage("Character 0x03a3 matches caselessly 0x03a3")
        .that(scanner.getPropertyValue(931))
        .isEqualTo(931);
    assertWithMessage("Character 0x03a4 matches caselessly 0x03a4")
        .that(scanner.getPropertyValue(932))
        .isEqualTo(932);
    assertWithMessage("Character 0x03a5 matches caselessly 0x03a5")
        .that(scanner.getPropertyValue(933))
        .isEqualTo(933);
    assertWithMessage("Character 0x03a6 matches caselessly 0x03a6")
        .that(scanner.getPropertyValue(934))
        .isEqualTo(934);
    assertWithMessage("Character 0x03a7 matches caselessly 0x03a7")
        .that(scanner.getPropertyValue(935))
        .isEqualTo(935);
    assertWithMessage("Character 0x03a8 matches caselessly 0x03a8")
        .that(scanner.getPropertyValue(936))
        .isEqualTo(936);
    assertWithMessage("Character 0x03a9 matches caselessly 0x03a9")
        .that(scanner.getPropertyValue(937))
        .isEqualTo(937);
    assertWithMessage("Character 0x03aa matches caselessly 0x03aa")
        .that(scanner.getPropertyValue(938))
        .isEqualTo(938);
    assertWithMessage("Character 0x03ab matches caselessly 0x03ab")
        .that(scanner.getPropertyValue(939))
        .isEqualTo(939);
    assertWithMessage("Character 0x03ac matches caselessly 0x0386")
        .that(scanner.getPropertyValue(940))
        .isEqualTo(902);
    assertWithMessage("Character 0x03ad matches caselessly 0x0388")
        .that(scanner.getPropertyValue(941))
        .isEqualTo(904);
    assertWithMessage("Character 0x03ae matches caselessly 0x0389")
        .that(scanner.getPropertyValue(942))
        .isEqualTo(905);
    assertWithMessage("Character 0x03af matches caselessly 0x038a")
        .that(scanner.getPropertyValue(943))
        .isEqualTo(906);
    assertWithMessage("Character 0x03b1 matches caselessly 0x0391")
        .that(scanner.getPropertyValue(945))
        .isEqualTo(913);
    assertWithMessage("Character 0x03b2 matches caselessly 0x0392")
        .that(scanner.getPropertyValue(946))
        .isEqualTo(914);
    assertWithMessage("Character 0x03b3 matches caselessly 0x0393")
        .that(scanner.getPropertyValue(947))
        .isEqualTo(915);
    assertWithMessage("Character 0x03b4 matches caselessly 0x0394")
        .that(scanner.getPropertyValue(948))
        .isEqualTo(916);
    assertWithMessage("Character 0x03b5 matches caselessly 0x0395")
        .that(scanner.getPropertyValue(949))
        .isEqualTo(917);
    assertWithMessage("Character 0x03b6 matches caselessly 0x0396")
        .that(scanner.getPropertyValue(950))
        .isEqualTo(918);
    assertWithMessage("Character 0x03b7 matches caselessly 0x0397")
        .that(scanner.getPropertyValue(951))
        .isEqualTo(919);
    assertWithMessage("Character 0x03b8 matches caselessly 0x0398")
        .that(scanner.getPropertyValue(952))
        .isEqualTo(920);
    assertWithMessage("Character 0x03b9 matches caselessly 0x0345")
        .that(scanner.getPropertyValue(953))
        .isEqualTo(837);
    assertWithMessage("Character 0x03ba matches caselessly 0x039a")
        .that(scanner.getPropertyValue(954))
        .isEqualTo(922);
    assertWithMessage("Character 0x03bb matches caselessly 0x039b")
        .that(scanner.getPropertyValue(955))
        .isEqualTo(923);
    assertWithMessage("Character 0x03bc matches caselessly 0x00b5")
        .that(scanner.getPropertyValue(956))
        .isEqualTo(181);
    assertWithMessage("Character 0x03bd matches caselessly 0x039d")
        .that(scanner.getPropertyValue(957))
        .isEqualTo(925);
    assertWithMessage("Character 0x03be matches caselessly 0x039e")
        .that(scanner.getPropertyValue(958))
        .isEqualTo(926);
    assertWithMessage("Character 0x03bf matches caselessly 0x039f")
        .that(scanner.getPropertyValue(959))
        .isEqualTo(927);
    assertWithMessage("Character 0x03c0 matches caselessly 0x03a0")
        .that(scanner.getPropertyValue(960))
        .isEqualTo(928);
    assertWithMessage("Character 0x03c1 matches caselessly 0x03a1")
        .that(scanner.getPropertyValue(961))
        .isEqualTo(929);
    assertWithMessage("Character 0x03c2 matches caselessly 0x03a3")
        .that(scanner.getPropertyValue(962))
        .isEqualTo(931);
    assertWithMessage("Character 0x03c3 matches caselessly 0x03a3")
        .that(scanner.getPropertyValue(963))
        .isEqualTo(931);
    assertWithMessage("Character 0x03c4 matches caselessly 0x03a4")
        .that(scanner.getPropertyValue(964))
        .isEqualTo(932);
    assertWithMessage("Character 0x03c5 matches caselessly 0x03a5")
        .that(scanner.getPropertyValue(965))
        .isEqualTo(933);
    assertWithMessage("Character 0x03c6 matches caselessly 0x03a6")
        .that(scanner.getPropertyValue(966))
        .isEqualTo(934);
    assertWithMessage("Character 0x03c7 matches caselessly 0x03a7")
        .that(scanner.getPropertyValue(967))
        .isEqualTo(935);
    assertWithMessage("Character 0x03c8 matches caselessly 0x03a8")
        .that(scanner.getPropertyValue(968))
        .isEqualTo(936);
    assertWithMessage("Character 0x03c9 matches caselessly 0x03a9")
        .that(scanner.getPropertyValue(969))
        .isEqualTo(937);
    assertWithMessage("Character 0x03ca matches caselessly 0x03aa")
        .that(scanner.getPropertyValue(970))
        .isEqualTo(938);
    assertWithMessage("Character 0x03cb matches caselessly 0x03ab")
        .that(scanner.getPropertyValue(971))
        .isEqualTo(939);
    assertWithMessage("Character 0x03cc matches caselessly 0x038c")
        .that(scanner.getPropertyValue(972))
        .isEqualTo(908);
    assertWithMessage("Character 0x03cd matches caselessly 0x038e")
        .that(scanner.getPropertyValue(973))
        .isEqualTo(910);
    assertWithMessage("Character 0x03ce matches caselessly 0x038f")
        .that(scanner.getPropertyValue(974))
        .isEqualTo(911);
    assertWithMessage("Character 0x03d0 matches caselessly 0x0392")
        .that(scanner.getPropertyValue(976))
        .isEqualTo(914);
    assertWithMessage("Character 0x03d1 matches caselessly 0x0398")
        .that(scanner.getPropertyValue(977))
        .isEqualTo(920);
    assertWithMessage("Character 0x03d5 matches caselessly 0x03a6")
        .that(scanner.getPropertyValue(981))
        .isEqualTo(934);
    assertWithMessage("Character 0x03d6 matches caselessly 0x03a0")
        .that(scanner.getPropertyValue(982))
        .isEqualTo(928);
    assertWithMessage("Character 0x03d8 matches caselessly 0x03d8")
        .that(scanner.getPropertyValue(984))
        .isEqualTo(984);
    assertWithMessage("Character 0x03d9 matches caselessly 0x03d8")
        .that(scanner.getPropertyValue(985))
        .isEqualTo(984);
    assertWithMessage("Character 0x03da matches caselessly 0x03da")
        .that(scanner.getPropertyValue(986))
        .isEqualTo(986);
    assertWithMessage("Character 0x03db matches caselessly 0x03da")
        .that(scanner.getPropertyValue(987))
        .isEqualTo(986);
    assertWithMessage("Character 0x03dc matches caselessly 0x03dc")
        .that(scanner.getPropertyValue(988))
        .isEqualTo(988);
    assertWithMessage("Character 0x03dd matches caselessly 0x03dc")
        .that(scanner.getPropertyValue(989))
        .isEqualTo(988);
    assertWithMessage("Character 0x03de matches caselessly 0x03de")
        .that(scanner.getPropertyValue(990))
        .isEqualTo(990);
    assertWithMessage("Character 0x03df matches caselessly 0x03de")
        .that(scanner.getPropertyValue(991))
        .isEqualTo(990);
    assertWithMessage("Character 0x03e0 matches caselessly 0x03e0")
        .that(scanner.getPropertyValue(992))
        .isEqualTo(992);
    assertWithMessage("Character 0x03e1 matches caselessly 0x03e0")
        .that(scanner.getPropertyValue(993))
        .isEqualTo(992);
    assertWithMessage("Character 0x03e2 matches caselessly 0x03e2")
        .that(scanner.getPropertyValue(994))
        .isEqualTo(994);
    assertWithMessage("Character 0x03e3 matches caselessly 0x03e2")
        .that(scanner.getPropertyValue(995))
        .isEqualTo(994);
    assertWithMessage("Character 0x03e4 matches caselessly 0x03e4")
        .that(scanner.getPropertyValue(996))
        .isEqualTo(996);
    assertWithMessage("Character 0x03e5 matches caselessly 0x03e4")
        .that(scanner.getPropertyValue(997))
        .isEqualTo(996);
    assertWithMessage("Character 0x03e6 matches caselessly 0x03e6")
        .that(scanner.getPropertyValue(998))
        .isEqualTo(998);
    assertWithMessage("Character 0x03e7 matches caselessly 0x03e6")
        .that(scanner.getPropertyValue(999))
        .isEqualTo(998);
    assertWithMessage("Character 0x03e8 matches caselessly 0x03e8")
        .that(scanner.getPropertyValue(1000))
        .isEqualTo(1000);
    assertWithMessage("Character 0x03e9 matches caselessly 0x03e8")
        .that(scanner.getPropertyValue(1001))
        .isEqualTo(1000);
    assertWithMessage("Character 0x03ea matches caselessly 0x03ea")
        .that(scanner.getPropertyValue(1002))
        .isEqualTo(1002);
    assertWithMessage("Character 0x03eb matches caselessly 0x03ea")
        .that(scanner.getPropertyValue(1003))
        .isEqualTo(1002);
    assertWithMessage("Character 0x03ec matches caselessly 0x03ec")
        .that(scanner.getPropertyValue(1004))
        .isEqualTo(1004);
    assertWithMessage("Character 0x03ed matches caselessly 0x03ec")
        .that(scanner.getPropertyValue(1005))
        .isEqualTo(1004);
    assertWithMessage("Character 0x03ee matches caselessly 0x03ee")
        .that(scanner.getPropertyValue(1006))
        .isEqualTo(1006);
    assertWithMessage("Character 0x03ef matches caselessly 0x03ee")
        .that(scanner.getPropertyValue(1007))
        .isEqualTo(1006);
    assertWithMessage("Character 0x03f0 matches caselessly 0x039a")
        .that(scanner.getPropertyValue(1008))
        .isEqualTo(922);
    assertWithMessage("Character 0x03f1 matches caselessly 0x03a1")
        .that(scanner.getPropertyValue(1009))
        .isEqualTo(929);
    assertWithMessage("Character 0x03f2 matches caselessly 0x03f2")
        .that(scanner.getPropertyValue(1010))
        .isEqualTo(1010);
    assertWithMessage("Character 0x03f4 matches caselessly 0x0398")
        .that(scanner.getPropertyValue(1012))
        .isEqualTo(920);
    assertWithMessage("Character 0x03f5 matches caselessly 0x0395")
        .that(scanner.getPropertyValue(1013))
        .isEqualTo(917);
    assertWithMessage("Character 0x03f7 matches caselessly 0x03f7")
        .that(scanner.getPropertyValue(1015))
        .isEqualTo(1015);
    assertWithMessage("Character 0x03f8 matches caselessly 0x03f7")
        .that(scanner.getPropertyValue(1016))
        .isEqualTo(1015);
    assertWithMessage("Character 0x03f9 matches caselessly 0x03f2")
        .that(scanner.getPropertyValue(1017))
        .isEqualTo(1010);
    assertWithMessage("Character 0x03fa matches caselessly 0x03fa")
        .that(scanner.getPropertyValue(1018))
        .isEqualTo(1018);
    assertWithMessage("Character 0x03fb matches caselessly 0x03fa")
        .that(scanner.getPropertyValue(1019))
        .isEqualTo(1018);
    assertWithMessage("Character 0x0400 matches caselessly 0x0400")
        .that(scanner.getPropertyValue(1024))
        .isEqualTo(1024);
    assertWithMessage("Character 0x0401 matches caselessly 0x0401")
        .that(scanner.getPropertyValue(1025))
        .isEqualTo(1025);
    assertWithMessage("Character 0x0402 matches caselessly 0x0402")
        .that(scanner.getPropertyValue(1026))
        .isEqualTo(1026);
    assertWithMessage("Character 0x0403 matches caselessly 0x0403")
        .that(scanner.getPropertyValue(1027))
        .isEqualTo(1027);
    assertWithMessage("Character 0x0404 matches caselessly 0x0404")
        .that(scanner.getPropertyValue(1028))
        .isEqualTo(1028);
    assertWithMessage("Character 0x0405 matches caselessly 0x0405")
        .that(scanner.getPropertyValue(1029))
        .isEqualTo(1029);
    assertWithMessage("Character 0x0406 matches caselessly 0x0406")
        .that(scanner.getPropertyValue(1030))
        .isEqualTo(1030);
    assertWithMessage("Character 0x0407 matches caselessly 0x0407")
        .that(scanner.getPropertyValue(1031))
        .isEqualTo(1031);
    assertWithMessage("Character 0x0408 matches caselessly 0x0408")
        .that(scanner.getPropertyValue(1032))
        .isEqualTo(1032);
    assertWithMessage("Character 0x0409 matches caselessly 0x0409")
        .that(scanner.getPropertyValue(1033))
        .isEqualTo(1033);
    assertWithMessage("Character 0x040a matches caselessly 0x040a")
        .that(scanner.getPropertyValue(1034))
        .isEqualTo(1034);
    assertWithMessage("Character 0x040b matches caselessly 0x040b")
        .that(scanner.getPropertyValue(1035))
        .isEqualTo(1035);
    assertWithMessage("Character 0x040c matches caselessly 0x040c")
        .that(scanner.getPropertyValue(1036))
        .isEqualTo(1036);
    assertWithMessage("Character 0x040d matches caselessly 0x040d")
        .that(scanner.getPropertyValue(1037))
        .isEqualTo(1037);
    assertWithMessage("Character 0x040e matches caselessly 0x040e")
        .that(scanner.getPropertyValue(1038))
        .isEqualTo(1038);
    assertWithMessage("Character 0x040f matches caselessly 0x040f")
        .that(scanner.getPropertyValue(1039))
        .isEqualTo(1039);
    assertWithMessage("Character 0x0410 matches caselessly 0x0410")
        .that(scanner.getPropertyValue(1040))
        .isEqualTo(1040);
    assertWithMessage("Character 0x0411 matches caselessly 0x0411")
        .that(scanner.getPropertyValue(1041))
        .isEqualTo(1041);
    assertWithMessage("Character 0x0412 matches caselessly 0x0412")
        .that(scanner.getPropertyValue(1042))
        .isEqualTo(1042);
    assertWithMessage("Character 0x0413 matches caselessly 0x0413")
        .that(scanner.getPropertyValue(1043))
        .isEqualTo(1043);
    assertWithMessage("Character 0x0414 matches caselessly 0x0414")
        .that(scanner.getPropertyValue(1044))
        .isEqualTo(1044);
    assertWithMessage("Character 0x0415 matches caselessly 0x0415")
        .that(scanner.getPropertyValue(1045))
        .isEqualTo(1045);
    assertWithMessage("Character 0x0416 matches caselessly 0x0416")
        .that(scanner.getPropertyValue(1046))
        .isEqualTo(1046);
    assertWithMessage("Character 0x0417 matches caselessly 0x0417")
        .that(scanner.getPropertyValue(1047))
        .isEqualTo(1047);
    assertWithMessage("Character 0x0418 matches caselessly 0x0418")
        .that(scanner.getPropertyValue(1048))
        .isEqualTo(1048);
    assertWithMessage("Character 0x0419 matches caselessly 0x0419")
        .that(scanner.getPropertyValue(1049))
        .isEqualTo(1049);
    assertWithMessage("Character 0x041a matches caselessly 0x041a")
        .that(scanner.getPropertyValue(1050))
        .isEqualTo(1050);
    assertWithMessage("Character 0x041b matches caselessly 0x041b")
        .that(scanner.getPropertyValue(1051))
        .isEqualTo(1051);
    assertWithMessage("Character 0x041c matches caselessly 0x041c")
        .that(scanner.getPropertyValue(1052))
        .isEqualTo(1052);
    assertWithMessage("Character 0x041d matches caselessly 0x041d")
        .that(scanner.getPropertyValue(1053))
        .isEqualTo(1053);
    assertWithMessage("Character 0x041e matches caselessly 0x041e")
        .that(scanner.getPropertyValue(1054))
        .isEqualTo(1054);
    assertWithMessage("Character 0x041f matches caselessly 0x041f")
        .that(scanner.getPropertyValue(1055))
        .isEqualTo(1055);
    assertWithMessage("Character 0x0420 matches caselessly 0x0420")
        .that(scanner.getPropertyValue(1056))
        .isEqualTo(1056);
    assertWithMessage("Character 0x0421 matches caselessly 0x0421")
        .that(scanner.getPropertyValue(1057))
        .isEqualTo(1057);
    assertWithMessage("Character 0x0422 matches caselessly 0x0422")
        .that(scanner.getPropertyValue(1058))
        .isEqualTo(1058);
    assertWithMessage("Character 0x0423 matches caselessly 0x0423")
        .that(scanner.getPropertyValue(1059))
        .isEqualTo(1059);
    assertWithMessage("Character 0x0424 matches caselessly 0x0424")
        .that(scanner.getPropertyValue(1060))
        .isEqualTo(1060);
    assertWithMessage("Character 0x0425 matches caselessly 0x0425")
        .that(scanner.getPropertyValue(1061))
        .isEqualTo(1061);
    assertWithMessage("Character 0x0426 matches caselessly 0x0426")
        .that(scanner.getPropertyValue(1062))
        .isEqualTo(1062);
    assertWithMessage("Character 0x0427 matches caselessly 0x0427")
        .that(scanner.getPropertyValue(1063))
        .isEqualTo(1063);
    assertWithMessage("Character 0x0428 matches caselessly 0x0428")
        .that(scanner.getPropertyValue(1064))
        .isEqualTo(1064);
    assertWithMessage("Character 0x0429 matches caselessly 0x0429")
        .that(scanner.getPropertyValue(1065))
        .isEqualTo(1065);
    assertWithMessage("Character 0x042a matches caselessly 0x042a")
        .that(scanner.getPropertyValue(1066))
        .isEqualTo(1066);
    assertWithMessage("Character 0x042b matches caselessly 0x042b")
        .that(scanner.getPropertyValue(1067))
        .isEqualTo(1067);
    assertWithMessage("Character 0x042c matches caselessly 0x042c")
        .that(scanner.getPropertyValue(1068))
        .isEqualTo(1068);
    assertWithMessage("Character 0x042d matches caselessly 0x042d")
        .that(scanner.getPropertyValue(1069))
        .isEqualTo(1069);
    assertWithMessage("Character 0x042e matches caselessly 0x042e")
        .that(scanner.getPropertyValue(1070))
        .isEqualTo(1070);
    assertWithMessage("Character 0x042f matches caselessly 0x042f")
        .that(scanner.getPropertyValue(1071))
        .isEqualTo(1071);
    assertWithMessage("Character 0x0430 matches caselessly 0x0410")
        .that(scanner.getPropertyValue(1072))
        .isEqualTo(1040);
    assertWithMessage("Character 0x0431 matches caselessly 0x0411")
        .that(scanner.getPropertyValue(1073))
        .isEqualTo(1041);
    assertWithMessage("Character 0x0432 matches caselessly 0x0412")
        .that(scanner.getPropertyValue(1074))
        .isEqualTo(1042);
    assertWithMessage("Character 0x0433 matches caselessly 0x0413")
        .that(scanner.getPropertyValue(1075))
        .isEqualTo(1043);
    assertWithMessage("Character 0x0434 matches caselessly 0x0414")
        .that(scanner.getPropertyValue(1076))
        .isEqualTo(1044);
    assertWithMessage("Character 0x0435 matches caselessly 0x0415")
        .that(scanner.getPropertyValue(1077))
        .isEqualTo(1045);
    assertWithMessage("Character 0x0436 matches caselessly 0x0416")
        .that(scanner.getPropertyValue(1078))
        .isEqualTo(1046);
    assertWithMessage("Character 0x0437 matches caselessly 0x0417")
        .that(scanner.getPropertyValue(1079))
        .isEqualTo(1047);
    assertWithMessage("Character 0x0438 matches caselessly 0x0418")
        .that(scanner.getPropertyValue(1080))
        .isEqualTo(1048);
    assertWithMessage("Character 0x0439 matches caselessly 0x0419")
        .that(scanner.getPropertyValue(1081))
        .isEqualTo(1049);
    assertWithMessage("Character 0x043a matches caselessly 0x041a")
        .that(scanner.getPropertyValue(1082))
        .isEqualTo(1050);
    assertWithMessage("Character 0x043b matches caselessly 0x041b")
        .that(scanner.getPropertyValue(1083))
        .isEqualTo(1051);
    assertWithMessage("Character 0x043c matches caselessly 0x041c")
        .that(scanner.getPropertyValue(1084))
        .isEqualTo(1052);
    assertWithMessage("Character 0x043d matches caselessly 0x041d")
        .that(scanner.getPropertyValue(1085))
        .isEqualTo(1053);
    assertWithMessage("Character 0x043e matches caselessly 0x041e")
        .that(scanner.getPropertyValue(1086))
        .isEqualTo(1054);
    assertWithMessage("Character 0x043f matches caselessly 0x041f")
        .that(scanner.getPropertyValue(1087))
        .isEqualTo(1055);
    assertWithMessage("Character 0x0440 matches caselessly 0x0420")
        .that(scanner.getPropertyValue(1088))
        .isEqualTo(1056);
    assertWithMessage("Character 0x0441 matches caselessly 0x0421")
        .that(scanner.getPropertyValue(1089))
        .isEqualTo(1057);
    assertWithMessage("Character 0x0442 matches caselessly 0x0422")
        .that(scanner.getPropertyValue(1090))
        .isEqualTo(1058);
    assertWithMessage("Character 0x0443 matches caselessly 0x0423")
        .that(scanner.getPropertyValue(1091))
        .isEqualTo(1059);
    assertWithMessage("Character 0x0444 matches caselessly 0x0424")
        .that(scanner.getPropertyValue(1092))
        .isEqualTo(1060);
    assertWithMessage("Character 0x0445 matches caselessly 0x0425")
        .that(scanner.getPropertyValue(1093))
        .isEqualTo(1061);
    assertWithMessage("Character 0x0446 matches caselessly 0x0426")
        .that(scanner.getPropertyValue(1094))
        .isEqualTo(1062);
    assertWithMessage("Character 0x0447 matches caselessly 0x0427")
        .that(scanner.getPropertyValue(1095))
        .isEqualTo(1063);
    assertWithMessage("Character 0x0448 matches caselessly 0x0428")
        .that(scanner.getPropertyValue(1096))
        .isEqualTo(1064);
    assertWithMessage("Character 0x0449 matches caselessly 0x0429")
        .that(scanner.getPropertyValue(1097))
        .isEqualTo(1065);
    assertWithMessage("Character 0x044a matches caselessly 0x042a")
        .that(scanner.getPropertyValue(1098))
        .isEqualTo(1066);
    assertWithMessage("Character 0x044b matches caselessly 0x042b")
        .that(scanner.getPropertyValue(1099))
        .isEqualTo(1067);
    assertWithMessage("Character 0x044c matches caselessly 0x042c")
        .that(scanner.getPropertyValue(1100))
        .isEqualTo(1068);
    assertWithMessage("Character 0x044d matches caselessly 0x042d")
        .that(scanner.getPropertyValue(1101))
        .isEqualTo(1069);
    assertWithMessage("Character 0x044e matches caselessly 0x042e")
        .that(scanner.getPropertyValue(1102))
        .isEqualTo(1070);
    assertWithMessage("Character 0x044f matches caselessly 0x042f")
        .that(scanner.getPropertyValue(1103))
        .isEqualTo(1071);
    assertWithMessage("Character 0x0450 matches caselessly 0x0400")
        .that(scanner.getPropertyValue(1104))
        .isEqualTo(1024);
    assertWithMessage("Character 0x0451 matches caselessly 0x0401")
        .that(scanner.getPropertyValue(1105))
        .isEqualTo(1025);
    assertWithMessage("Character 0x0452 matches caselessly 0x0402")
        .that(scanner.getPropertyValue(1106))
        .isEqualTo(1026);
    assertWithMessage("Character 0x0453 matches caselessly 0x0403")
        .that(scanner.getPropertyValue(1107))
        .isEqualTo(1027);
    assertWithMessage("Character 0x0454 matches caselessly 0x0404")
        .that(scanner.getPropertyValue(1108))
        .isEqualTo(1028);
    assertWithMessage("Character 0x0455 matches caselessly 0x0405")
        .that(scanner.getPropertyValue(1109))
        .isEqualTo(1029);
    assertWithMessage("Character 0x0456 matches caselessly 0x0406")
        .that(scanner.getPropertyValue(1110))
        .isEqualTo(1030);
    assertWithMessage("Character 0x0457 matches caselessly 0x0407")
        .that(scanner.getPropertyValue(1111))
        .isEqualTo(1031);
    assertWithMessage("Character 0x0458 matches caselessly 0x0408")
        .that(scanner.getPropertyValue(1112))
        .isEqualTo(1032);
    assertWithMessage("Character 0x0459 matches caselessly 0x0409")
        .that(scanner.getPropertyValue(1113))
        .isEqualTo(1033);
    assertWithMessage("Character 0x045a matches caselessly 0x040a")
        .that(scanner.getPropertyValue(1114))
        .isEqualTo(1034);
    assertWithMessage("Character 0x045b matches caselessly 0x040b")
        .that(scanner.getPropertyValue(1115))
        .isEqualTo(1035);
    assertWithMessage("Character 0x045c matches caselessly 0x040c")
        .that(scanner.getPropertyValue(1116))
        .isEqualTo(1036);
    assertWithMessage("Character 0x045d matches caselessly 0x040d")
        .that(scanner.getPropertyValue(1117))
        .isEqualTo(1037);
    assertWithMessage("Character 0x045e matches caselessly 0x040e")
        .that(scanner.getPropertyValue(1118))
        .isEqualTo(1038);
    assertWithMessage("Character 0x045f matches caselessly 0x040f")
        .that(scanner.getPropertyValue(1119))
        .isEqualTo(1039);
    assertWithMessage("Character 0x0460 matches caselessly 0x0460")
        .that(scanner.getPropertyValue(1120))
        .isEqualTo(1120);
    assertWithMessage("Character 0x0461 matches caselessly 0x0460")
        .that(scanner.getPropertyValue(1121))
        .isEqualTo(1120);
    assertWithMessage("Character 0x0462 matches caselessly 0x0462")
        .that(scanner.getPropertyValue(1122))
        .isEqualTo(1122);
    assertWithMessage("Character 0x0463 matches caselessly 0x0462")
        .that(scanner.getPropertyValue(1123))
        .isEqualTo(1122);
    assertWithMessage("Character 0x0464 matches caselessly 0x0464")
        .that(scanner.getPropertyValue(1124))
        .isEqualTo(1124);
    assertWithMessage("Character 0x0465 matches caselessly 0x0464")
        .that(scanner.getPropertyValue(1125))
        .isEqualTo(1124);
    assertWithMessage("Character 0x0466 matches caselessly 0x0466")
        .that(scanner.getPropertyValue(1126))
        .isEqualTo(1126);
    assertWithMessage("Character 0x0467 matches caselessly 0x0466")
        .that(scanner.getPropertyValue(1127))
        .isEqualTo(1126);
    assertWithMessage("Character 0x0468 matches caselessly 0x0468")
        .that(scanner.getPropertyValue(1128))
        .isEqualTo(1128);
    assertWithMessage("Character 0x0469 matches caselessly 0x0468")
        .that(scanner.getPropertyValue(1129))
        .isEqualTo(1128);
    assertWithMessage("Character 0x046a matches caselessly 0x046a")
        .that(scanner.getPropertyValue(1130))
        .isEqualTo(1130);
    assertWithMessage("Character 0x046b matches caselessly 0x046a")
        .that(scanner.getPropertyValue(1131))
        .isEqualTo(1130);
    assertWithMessage("Character 0x046c matches caselessly 0x046c")
        .that(scanner.getPropertyValue(1132))
        .isEqualTo(1132);
    assertWithMessage("Character 0x046d matches caselessly 0x046c")
        .that(scanner.getPropertyValue(1133))
        .isEqualTo(1132);
    assertWithMessage("Character 0x046e matches caselessly 0x046e")
        .that(scanner.getPropertyValue(1134))
        .isEqualTo(1134);
    assertWithMessage("Character 0x046f matches caselessly 0x046e")
        .that(scanner.getPropertyValue(1135))
        .isEqualTo(1134);
    assertWithMessage("Character 0x0470 matches caselessly 0x0470")
        .that(scanner.getPropertyValue(1136))
        .isEqualTo(1136);
    assertWithMessage("Character 0x0471 matches caselessly 0x0470")
        .that(scanner.getPropertyValue(1137))
        .isEqualTo(1136);
    assertWithMessage("Character 0x0472 matches caselessly 0x0472")
        .that(scanner.getPropertyValue(1138))
        .isEqualTo(1138);
    assertWithMessage("Character 0x0473 matches caselessly 0x0472")
        .that(scanner.getPropertyValue(1139))
        .isEqualTo(1138);
    assertWithMessage("Character 0x0474 matches caselessly 0x0474")
        .that(scanner.getPropertyValue(1140))
        .isEqualTo(1140);
    assertWithMessage("Character 0x0475 matches caselessly 0x0474")
        .that(scanner.getPropertyValue(1141))
        .isEqualTo(1140);
    assertWithMessage("Character 0x0476 matches caselessly 0x0476")
        .that(scanner.getPropertyValue(1142))
        .isEqualTo(1142);
    assertWithMessage("Character 0x0477 matches caselessly 0x0476")
        .that(scanner.getPropertyValue(1143))
        .isEqualTo(1142);
    assertWithMessage("Character 0x0478 matches caselessly 0x0478")
        .that(scanner.getPropertyValue(1144))
        .isEqualTo(1144);
    assertWithMessage("Character 0x0479 matches caselessly 0x0478")
        .that(scanner.getPropertyValue(1145))
        .isEqualTo(1144);
    assertWithMessage("Character 0x047a matches caselessly 0x047a")
        .that(scanner.getPropertyValue(1146))
        .isEqualTo(1146);
    assertWithMessage("Character 0x047b matches caselessly 0x047a")
        .that(scanner.getPropertyValue(1147))
        .isEqualTo(1146);
    assertWithMessage("Character 0x047c matches caselessly 0x047c")
        .that(scanner.getPropertyValue(1148))
        .isEqualTo(1148);
    assertWithMessage("Character 0x047d matches caselessly 0x047c")
        .that(scanner.getPropertyValue(1149))
        .isEqualTo(1148);
    assertWithMessage("Character 0x047e matches caselessly 0x047e")
        .that(scanner.getPropertyValue(1150))
        .isEqualTo(1150);
    assertWithMessage("Character 0x047f matches caselessly 0x047e")
        .that(scanner.getPropertyValue(1151))
        .isEqualTo(1150);
    assertWithMessage("Character 0x0480 matches caselessly 0x0480")
        .that(scanner.getPropertyValue(1152))
        .isEqualTo(1152);
    assertWithMessage("Character 0x0481 matches caselessly 0x0480")
        .that(scanner.getPropertyValue(1153))
        .isEqualTo(1152);
    assertWithMessage("Character 0x048a matches caselessly 0x048a")
        .that(scanner.getPropertyValue(1162))
        .isEqualTo(1162);
    assertWithMessage("Character 0x048b matches caselessly 0x048a")
        .that(scanner.getPropertyValue(1163))
        .isEqualTo(1162);
    assertWithMessage("Character 0x048c matches caselessly 0x048c")
        .that(scanner.getPropertyValue(1164))
        .isEqualTo(1164);
    assertWithMessage("Character 0x048d matches caselessly 0x048c")
        .that(scanner.getPropertyValue(1165))
        .isEqualTo(1164);
    assertWithMessage("Character 0x048e matches caselessly 0x048e")
        .that(scanner.getPropertyValue(1166))
        .isEqualTo(1166);
    assertWithMessage("Character 0x048f matches caselessly 0x048e")
        .that(scanner.getPropertyValue(1167))
        .isEqualTo(1166);
    assertWithMessage("Character 0x0490 matches caselessly 0x0490")
        .that(scanner.getPropertyValue(1168))
        .isEqualTo(1168);
    assertWithMessage("Character 0x0491 matches caselessly 0x0490")
        .that(scanner.getPropertyValue(1169))
        .isEqualTo(1168);
    assertWithMessage("Character 0x0492 matches caselessly 0x0492")
        .that(scanner.getPropertyValue(1170))
        .isEqualTo(1170);
    assertWithMessage("Character 0x0493 matches caselessly 0x0492")
        .that(scanner.getPropertyValue(1171))
        .isEqualTo(1170);
    assertWithMessage("Character 0x0494 matches caselessly 0x0494")
        .that(scanner.getPropertyValue(1172))
        .isEqualTo(1172);
    assertWithMessage("Character 0x0495 matches caselessly 0x0494")
        .that(scanner.getPropertyValue(1173))
        .isEqualTo(1172);
    assertWithMessage("Character 0x0496 matches caselessly 0x0496")
        .that(scanner.getPropertyValue(1174))
        .isEqualTo(1174);
    assertWithMessage("Character 0x0497 matches caselessly 0x0496")
        .that(scanner.getPropertyValue(1175))
        .isEqualTo(1174);
    assertWithMessage("Character 0x0498 matches caselessly 0x0498")
        .that(scanner.getPropertyValue(1176))
        .isEqualTo(1176);
    assertWithMessage("Character 0x0499 matches caselessly 0x0498")
        .that(scanner.getPropertyValue(1177))
        .isEqualTo(1176);
    assertWithMessage("Character 0x049a matches caselessly 0x049a")
        .that(scanner.getPropertyValue(1178))
        .isEqualTo(1178);
    assertWithMessage("Character 0x049b matches caselessly 0x049a")
        .that(scanner.getPropertyValue(1179))
        .isEqualTo(1178);
    assertWithMessage("Character 0x049c matches caselessly 0x049c")
        .that(scanner.getPropertyValue(1180))
        .isEqualTo(1180);
    assertWithMessage("Character 0x049d matches caselessly 0x049c")
        .that(scanner.getPropertyValue(1181))
        .isEqualTo(1180);
    assertWithMessage("Character 0x049e matches caselessly 0x049e")
        .that(scanner.getPropertyValue(1182))
        .isEqualTo(1182);
    assertWithMessage("Character 0x049f matches caselessly 0x049e")
        .that(scanner.getPropertyValue(1183))
        .isEqualTo(1182);
    assertWithMessage("Character 0x04a0 matches caselessly 0x04a0")
        .that(scanner.getPropertyValue(1184))
        .isEqualTo(1184);
    assertWithMessage("Character 0x04a1 matches caselessly 0x04a0")
        .that(scanner.getPropertyValue(1185))
        .isEqualTo(1184);
    assertWithMessage("Character 0x04a2 matches caselessly 0x04a2")
        .that(scanner.getPropertyValue(1186))
        .isEqualTo(1186);
    assertWithMessage("Character 0x04a3 matches caselessly 0x04a2")
        .that(scanner.getPropertyValue(1187))
        .isEqualTo(1186);
    assertWithMessage("Character 0x04a4 matches caselessly 0x04a4")
        .that(scanner.getPropertyValue(1188))
        .isEqualTo(1188);
    assertWithMessage("Character 0x04a5 matches caselessly 0x04a4")
        .that(scanner.getPropertyValue(1189))
        .isEqualTo(1188);
    assertWithMessage("Character 0x04a6 matches caselessly 0x04a6")
        .that(scanner.getPropertyValue(1190))
        .isEqualTo(1190);
    assertWithMessage("Character 0x04a7 matches caselessly 0x04a6")
        .that(scanner.getPropertyValue(1191))
        .isEqualTo(1190);
    assertWithMessage("Character 0x04a8 matches caselessly 0x04a8")
        .that(scanner.getPropertyValue(1192))
        .isEqualTo(1192);
    assertWithMessage("Character 0x04a9 matches caselessly 0x04a8")
        .that(scanner.getPropertyValue(1193))
        .isEqualTo(1192);
    assertWithMessage("Character 0x04aa matches caselessly 0x04aa")
        .that(scanner.getPropertyValue(1194))
        .isEqualTo(1194);
    assertWithMessage("Character 0x04ab matches caselessly 0x04aa")
        .that(scanner.getPropertyValue(1195))
        .isEqualTo(1194);
    assertWithMessage("Character 0x04ac matches caselessly 0x04ac")
        .that(scanner.getPropertyValue(1196))
        .isEqualTo(1196);
    assertWithMessage("Character 0x04ad matches caselessly 0x04ac")
        .that(scanner.getPropertyValue(1197))
        .isEqualTo(1196);
    assertWithMessage("Character 0x04ae matches caselessly 0x04ae")
        .that(scanner.getPropertyValue(1198))
        .isEqualTo(1198);
    assertWithMessage("Character 0x04af matches caselessly 0x04ae")
        .that(scanner.getPropertyValue(1199))
        .isEqualTo(1198);
    assertWithMessage("Character 0x04b0 matches caselessly 0x04b0")
        .that(scanner.getPropertyValue(1200))
        .isEqualTo(1200);
    assertWithMessage("Character 0x04b1 matches caselessly 0x04b0")
        .that(scanner.getPropertyValue(1201))
        .isEqualTo(1200);
    assertWithMessage("Character 0x04b2 matches caselessly 0x04b2")
        .that(scanner.getPropertyValue(1202))
        .isEqualTo(1202);
    assertWithMessage("Character 0x04b3 matches caselessly 0x04b2")
        .that(scanner.getPropertyValue(1203))
        .isEqualTo(1202);
    assertWithMessage("Character 0x04b4 matches caselessly 0x04b4")
        .that(scanner.getPropertyValue(1204))
        .isEqualTo(1204);
    assertWithMessage("Character 0x04b5 matches caselessly 0x04b4")
        .that(scanner.getPropertyValue(1205))
        .isEqualTo(1204);
    assertWithMessage("Character 0x04b6 matches caselessly 0x04b6")
        .that(scanner.getPropertyValue(1206))
        .isEqualTo(1206);
    assertWithMessage("Character 0x04b7 matches caselessly 0x04b6")
        .that(scanner.getPropertyValue(1207))
        .isEqualTo(1206);
    assertWithMessage("Character 0x04b8 matches caselessly 0x04b8")
        .that(scanner.getPropertyValue(1208))
        .isEqualTo(1208);
    assertWithMessage("Character 0x04b9 matches caselessly 0x04b8")
        .that(scanner.getPropertyValue(1209))
        .isEqualTo(1208);
    assertWithMessage("Character 0x04ba matches caselessly 0x04ba")
        .that(scanner.getPropertyValue(1210))
        .isEqualTo(1210);
    assertWithMessage("Character 0x04bb matches caselessly 0x04ba")
        .that(scanner.getPropertyValue(1211))
        .isEqualTo(1210);
    assertWithMessage("Character 0x04bc matches caselessly 0x04bc")
        .that(scanner.getPropertyValue(1212))
        .isEqualTo(1212);
    assertWithMessage("Character 0x04bd matches caselessly 0x04bc")
        .that(scanner.getPropertyValue(1213))
        .isEqualTo(1212);
    assertWithMessage("Character 0x04be matches caselessly 0x04be")
        .that(scanner.getPropertyValue(1214))
        .isEqualTo(1214);
    assertWithMessage("Character 0x04bf matches caselessly 0x04be")
        .that(scanner.getPropertyValue(1215))
        .isEqualTo(1214);
    assertWithMessage("Character 0x04c1 matches caselessly 0x04c1")
        .that(scanner.getPropertyValue(1217))
        .isEqualTo(1217);
    assertWithMessage("Character 0x04c2 matches caselessly 0x04c1")
        .that(scanner.getPropertyValue(1218))
        .isEqualTo(1217);
    assertWithMessage("Character 0x04c3 matches caselessly 0x04c3")
        .that(scanner.getPropertyValue(1219))
        .isEqualTo(1219);
    assertWithMessage("Character 0x04c4 matches caselessly 0x04c3")
        .that(scanner.getPropertyValue(1220))
        .isEqualTo(1219);
    assertWithMessage("Character 0x04c5 matches caselessly 0x04c5")
        .that(scanner.getPropertyValue(1221))
        .isEqualTo(1221);
    assertWithMessage("Character 0x04c6 matches caselessly 0x04c5")
        .that(scanner.getPropertyValue(1222))
        .isEqualTo(1221);
    assertWithMessage("Character 0x04c7 matches caselessly 0x04c7")
        .that(scanner.getPropertyValue(1223))
        .isEqualTo(1223);
    assertWithMessage("Character 0x04c8 matches caselessly 0x04c7")
        .that(scanner.getPropertyValue(1224))
        .isEqualTo(1223);
    assertWithMessage("Character 0x04c9 matches caselessly 0x04c9")
        .that(scanner.getPropertyValue(1225))
        .isEqualTo(1225);
    assertWithMessage("Character 0x04ca matches caselessly 0x04c9")
        .that(scanner.getPropertyValue(1226))
        .isEqualTo(1225);
    assertWithMessage("Character 0x04cb matches caselessly 0x04cb")
        .that(scanner.getPropertyValue(1227))
        .isEqualTo(1227);
    assertWithMessage("Character 0x04cc matches caselessly 0x04cb")
        .that(scanner.getPropertyValue(1228))
        .isEqualTo(1227);
    assertWithMessage("Character 0x04cd matches caselessly 0x04cd")
        .that(scanner.getPropertyValue(1229))
        .isEqualTo(1229);
    assertWithMessage("Character 0x04ce matches caselessly 0x04cd")
        .that(scanner.getPropertyValue(1230))
        .isEqualTo(1229);
    assertWithMessage("Character 0x04d0 matches caselessly 0x04d0")
        .that(scanner.getPropertyValue(1232))
        .isEqualTo(1232);
    assertWithMessage("Character 0x04d1 matches caselessly 0x04d0")
        .that(scanner.getPropertyValue(1233))
        .isEqualTo(1232);
    assertWithMessage("Character 0x04d2 matches caselessly 0x04d2")
        .that(scanner.getPropertyValue(1234))
        .isEqualTo(1234);
    assertWithMessage("Character 0x04d3 matches caselessly 0x04d2")
        .that(scanner.getPropertyValue(1235))
        .isEqualTo(1234);
    assertWithMessage("Character 0x04d4 matches caselessly 0x04d4")
        .that(scanner.getPropertyValue(1236))
        .isEqualTo(1236);
    assertWithMessage("Character 0x04d5 matches caselessly 0x04d4")
        .that(scanner.getPropertyValue(1237))
        .isEqualTo(1236);
    assertWithMessage("Character 0x04d6 matches caselessly 0x04d6")
        .that(scanner.getPropertyValue(1238))
        .isEqualTo(1238);
    assertWithMessage("Character 0x04d7 matches caselessly 0x04d6")
        .that(scanner.getPropertyValue(1239))
        .isEqualTo(1238);
    assertWithMessage("Character 0x04d8 matches caselessly 0x04d8")
        .that(scanner.getPropertyValue(1240))
        .isEqualTo(1240);
    assertWithMessage("Character 0x04d9 matches caselessly 0x04d8")
        .that(scanner.getPropertyValue(1241))
        .isEqualTo(1240);
    assertWithMessage("Character 0x04da matches caselessly 0x04da")
        .that(scanner.getPropertyValue(1242))
        .isEqualTo(1242);
    assertWithMessage("Character 0x04db matches caselessly 0x04da")
        .that(scanner.getPropertyValue(1243))
        .isEqualTo(1242);
    assertWithMessage("Character 0x04dc matches caselessly 0x04dc")
        .that(scanner.getPropertyValue(1244))
        .isEqualTo(1244);
    assertWithMessage("Character 0x04dd matches caselessly 0x04dc")
        .that(scanner.getPropertyValue(1245))
        .isEqualTo(1244);
    assertWithMessage("Character 0x04de matches caselessly 0x04de")
        .that(scanner.getPropertyValue(1246))
        .isEqualTo(1246);
    assertWithMessage("Character 0x04df matches caselessly 0x04de")
        .that(scanner.getPropertyValue(1247))
        .isEqualTo(1246);
    assertWithMessage("Character 0x04e0 matches caselessly 0x04e0")
        .that(scanner.getPropertyValue(1248))
        .isEqualTo(1248);
    assertWithMessage("Character 0x04e1 matches caselessly 0x04e0")
        .that(scanner.getPropertyValue(1249))
        .isEqualTo(1248);
    assertWithMessage("Character 0x04e2 matches caselessly 0x04e2")
        .that(scanner.getPropertyValue(1250))
        .isEqualTo(1250);
    assertWithMessage("Character 0x04e3 matches caselessly 0x04e2")
        .that(scanner.getPropertyValue(1251))
        .isEqualTo(1250);
    assertWithMessage("Character 0x04e4 matches caselessly 0x04e4")
        .that(scanner.getPropertyValue(1252))
        .isEqualTo(1252);
    assertWithMessage("Character 0x04e5 matches caselessly 0x04e4")
        .that(scanner.getPropertyValue(1253))
        .isEqualTo(1252);
    assertWithMessage("Character 0x04e6 matches caselessly 0x04e6")
        .that(scanner.getPropertyValue(1254))
        .isEqualTo(1254);
    assertWithMessage("Character 0x04e7 matches caselessly 0x04e6")
        .that(scanner.getPropertyValue(1255))
        .isEqualTo(1254);
    assertWithMessage("Character 0x04e8 matches caselessly 0x04e8")
        .that(scanner.getPropertyValue(1256))
        .isEqualTo(1256);
    assertWithMessage("Character 0x04e9 matches caselessly 0x04e8")
        .that(scanner.getPropertyValue(1257))
        .isEqualTo(1256);
    assertWithMessage("Character 0x04ea matches caselessly 0x04ea")
        .that(scanner.getPropertyValue(1258))
        .isEqualTo(1258);
    assertWithMessage("Character 0x04eb matches caselessly 0x04ea")
        .that(scanner.getPropertyValue(1259))
        .isEqualTo(1258);
    assertWithMessage("Character 0x04ec matches caselessly 0x04ec")
        .that(scanner.getPropertyValue(1260))
        .isEqualTo(1260);
    assertWithMessage("Character 0x04ed matches caselessly 0x04ec")
        .that(scanner.getPropertyValue(1261))
        .isEqualTo(1260);
    assertWithMessage("Character 0x04ee matches caselessly 0x04ee")
        .that(scanner.getPropertyValue(1262))
        .isEqualTo(1262);
    assertWithMessage("Character 0x04ef matches caselessly 0x04ee")
        .that(scanner.getPropertyValue(1263))
        .isEqualTo(1262);
    assertWithMessage("Character 0x04f0 matches caselessly 0x04f0")
        .that(scanner.getPropertyValue(1264))
        .isEqualTo(1264);
    assertWithMessage("Character 0x04f1 matches caselessly 0x04f0")
        .that(scanner.getPropertyValue(1265))
        .isEqualTo(1264);
    assertWithMessage("Character 0x04f2 matches caselessly 0x04f2")
        .that(scanner.getPropertyValue(1266))
        .isEqualTo(1266);
    assertWithMessage("Character 0x04f3 matches caselessly 0x04f2")
        .that(scanner.getPropertyValue(1267))
        .isEqualTo(1266);
    assertWithMessage("Character 0x04f4 matches caselessly 0x04f4")
        .that(scanner.getPropertyValue(1268))
        .isEqualTo(1268);
    assertWithMessage("Character 0x04f5 matches caselessly 0x04f4")
        .that(scanner.getPropertyValue(1269))
        .isEqualTo(1268);
    assertWithMessage("Character 0x04f6 matches caselessly 0x04f6")
        .that(scanner.getPropertyValue(1270))
        .isEqualTo(1270);
    assertWithMessage("Character 0x04f7 matches caselessly 0x04f6")
        .that(scanner.getPropertyValue(1271))
        .isEqualTo(1270);
    assertWithMessage("Character 0x04f8 matches caselessly 0x04f8")
        .that(scanner.getPropertyValue(1272))
        .isEqualTo(1272);
    assertWithMessage("Character 0x04f9 matches caselessly 0x04f8")
        .that(scanner.getPropertyValue(1273))
        .isEqualTo(1272);
    assertWithMessage("Character 0x0500 matches caselessly 0x0500")
        .that(scanner.getPropertyValue(1280))
        .isEqualTo(1280);
    assertWithMessage("Character 0x0501 matches caselessly 0x0500")
        .that(scanner.getPropertyValue(1281))
        .isEqualTo(1280);
    assertWithMessage("Character 0x0502 matches caselessly 0x0502")
        .that(scanner.getPropertyValue(1282))
        .isEqualTo(1282);
    assertWithMessage("Character 0x0503 matches caselessly 0x0502")
        .that(scanner.getPropertyValue(1283))
        .isEqualTo(1282);
    assertWithMessage("Character 0x0504 matches caselessly 0x0504")
        .that(scanner.getPropertyValue(1284))
        .isEqualTo(1284);
    assertWithMessage("Character 0x0505 matches caselessly 0x0504")
        .that(scanner.getPropertyValue(1285))
        .isEqualTo(1284);
    assertWithMessage("Character 0x0506 matches caselessly 0x0506")
        .that(scanner.getPropertyValue(1286))
        .isEqualTo(1286);
    assertWithMessage("Character 0x0507 matches caselessly 0x0506")
        .that(scanner.getPropertyValue(1287))
        .isEqualTo(1286);
    assertWithMessage("Character 0x0508 matches caselessly 0x0508")
        .that(scanner.getPropertyValue(1288))
        .isEqualTo(1288);
    assertWithMessage("Character 0x0509 matches caselessly 0x0508")
        .that(scanner.getPropertyValue(1289))
        .isEqualTo(1288);
    assertWithMessage("Character 0x050a matches caselessly 0x050a")
        .that(scanner.getPropertyValue(1290))
        .isEqualTo(1290);
    assertWithMessage("Character 0x050b matches caselessly 0x050a")
        .that(scanner.getPropertyValue(1291))
        .isEqualTo(1290);
    assertWithMessage("Character 0x050c matches caselessly 0x050c")
        .that(scanner.getPropertyValue(1292))
        .isEqualTo(1292);
    assertWithMessage("Character 0x050d matches caselessly 0x050c")
        .that(scanner.getPropertyValue(1293))
        .isEqualTo(1292);
    assertWithMessage("Character 0x050e matches caselessly 0x050e")
        .that(scanner.getPropertyValue(1294))
        .isEqualTo(1294);
    assertWithMessage("Character 0x050f matches caselessly 0x050e")
        .that(scanner.getPropertyValue(1295))
        .isEqualTo(1294);
    assertWithMessage("Character 0x0531 matches caselessly 0x0531")
        .that(scanner.getPropertyValue(1329))
        .isEqualTo(1329);
    assertWithMessage("Character 0x0532 matches caselessly 0x0532")
        .that(scanner.getPropertyValue(1330))
        .isEqualTo(1330);
    assertWithMessage("Character 0x0533 matches caselessly 0x0533")
        .that(scanner.getPropertyValue(1331))
        .isEqualTo(1331);
    assertWithMessage("Character 0x0534 matches caselessly 0x0534")
        .that(scanner.getPropertyValue(1332))
        .isEqualTo(1332);
    assertWithMessage("Character 0x0535 matches caselessly 0x0535")
        .that(scanner.getPropertyValue(1333))
        .isEqualTo(1333);
    assertWithMessage("Character 0x0536 matches caselessly 0x0536")
        .that(scanner.getPropertyValue(1334))
        .isEqualTo(1334);
    assertWithMessage("Character 0x0537 matches caselessly 0x0537")
        .that(scanner.getPropertyValue(1335))
        .isEqualTo(1335);
    assertWithMessage("Character 0x0538 matches caselessly 0x0538")
        .that(scanner.getPropertyValue(1336))
        .isEqualTo(1336);
    assertWithMessage("Character 0x0539 matches caselessly 0x0539")
        .that(scanner.getPropertyValue(1337))
        .isEqualTo(1337);
    assertWithMessage("Character 0x053a matches caselessly 0x053a")
        .that(scanner.getPropertyValue(1338))
        .isEqualTo(1338);
    assertWithMessage("Character 0x053b matches caselessly 0x053b")
        .that(scanner.getPropertyValue(1339))
        .isEqualTo(1339);
    assertWithMessage("Character 0x053c matches caselessly 0x053c")
        .that(scanner.getPropertyValue(1340))
        .isEqualTo(1340);
    assertWithMessage("Character 0x053d matches caselessly 0x053d")
        .that(scanner.getPropertyValue(1341))
        .isEqualTo(1341);
    assertWithMessage("Character 0x053e matches caselessly 0x053e")
        .that(scanner.getPropertyValue(1342))
        .isEqualTo(1342);
    assertWithMessage("Character 0x053f matches caselessly 0x053f")
        .that(scanner.getPropertyValue(1343))
        .isEqualTo(1343);
    assertWithMessage("Character 0x0540 matches caselessly 0x0540")
        .that(scanner.getPropertyValue(1344))
        .isEqualTo(1344);
    assertWithMessage("Character 0x0541 matches caselessly 0x0541")
        .that(scanner.getPropertyValue(1345))
        .isEqualTo(1345);
    assertWithMessage("Character 0x0542 matches caselessly 0x0542")
        .that(scanner.getPropertyValue(1346))
        .isEqualTo(1346);
    assertWithMessage("Character 0x0543 matches caselessly 0x0543")
        .that(scanner.getPropertyValue(1347))
        .isEqualTo(1347);
    assertWithMessage("Character 0x0544 matches caselessly 0x0544")
        .that(scanner.getPropertyValue(1348))
        .isEqualTo(1348);
    assertWithMessage("Character 0x0545 matches caselessly 0x0545")
        .that(scanner.getPropertyValue(1349))
        .isEqualTo(1349);
    assertWithMessage("Character 0x0546 matches caselessly 0x0546")
        .that(scanner.getPropertyValue(1350))
        .isEqualTo(1350);
    assertWithMessage("Character 0x0547 matches caselessly 0x0547")
        .that(scanner.getPropertyValue(1351))
        .isEqualTo(1351);
    assertWithMessage("Character 0x0548 matches caselessly 0x0548")
        .that(scanner.getPropertyValue(1352))
        .isEqualTo(1352);
    assertWithMessage("Character 0x0549 matches caselessly 0x0549")
        .that(scanner.getPropertyValue(1353))
        .isEqualTo(1353);
    assertWithMessage("Character 0x054a matches caselessly 0x054a")
        .that(scanner.getPropertyValue(1354))
        .isEqualTo(1354);
    assertWithMessage("Character 0x054b matches caselessly 0x054b")
        .that(scanner.getPropertyValue(1355))
        .isEqualTo(1355);
    assertWithMessage("Character 0x054c matches caselessly 0x054c")
        .that(scanner.getPropertyValue(1356))
        .isEqualTo(1356);
    assertWithMessage("Character 0x054d matches caselessly 0x054d")
        .that(scanner.getPropertyValue(1357))
        .isEqualTo(1357);
    assertWithMessage("Character 0x054e matches caselessly 0x054e")
        .that(scanner.getPropertyValue(1358))
        .isEqualTo(1358);
    assertWithMessage("Character 0x054f matches caselessly 0x054f")
        .that(scanner.getPropertyValue(1359))
        .isEqualTo(1359);
    assertWithMessage("Character 0x0550 matches caselessly 0x0550")
        .that(scanner.getPropertyValue(1360))
        .isEqualTo(1360);
    assertWithMessage("Character 0x0551 matches caselessly 0x0551")
        .that(scanner.getPropertyValue(1361))
        .isEqualTo(1361);
    assertWithMessage("Character 0x0552 matches caselessly 0x0552")
        .that(scanner.getPropertyValue(1362))
        .isEqualTo(1362);
    assertWithMessage("Character 0x0553 matches caselessly 0x0553")
        .that(scanner.getPropertyValue(1363))
        .isEqualTo(1363);
    assertWithMessage("Character 0x0554 matches caselessly 0x0554")
        .that(scanner.getPropertyValue(1364))
        .isEqualTo(1364);
    assertWithMessage("Character 0x0555 matches caselessly 0x0555")
        .that(scanner.getPropertyValue(1365))
        .isEqualTo(1365);
    assertWithMessage("Character 0x0556 matches caselessly 0x0556")
        .that(scanner.getPropertyValue(1366))
        .isEqualTo(1366);
    assertWithMessage("Character 0x0561 matches caselessly 0x0531")
        .that(scanner.getPropertyValue(1377))
        .isEqualTo(1329);
    assertWithMessage("Character 0x0562 matches caselessly 0x0532")
        .that(scanner.getPropertyValue(1378))
        .isEqualTo(1330);
    assertWithMessage("Character 0x0563 matches caselessly 0x0533")
        .that(scanner.getPropertyValue(1379))
        .isEqualTo(1331);
    assertWithMessage("Character 0x0564 matches caselessly 0x0534")
        .that(scanner.getPropertyValue(1380))
        .isEqualTo(1332);
    assertWithMessage("Character 0x0565 matches caselessly 0x0535")
        .that(scanner.getPropertyValue(1381))
        .isEqualTo(1333);
    assertWithMessage("Character 0x0566 matches caselessly 0x0536")
        .that(scanner.getPropertyValue(1382))
        .isEqualTo(1334);
    assertWithMessage("Character 0x0567 matches caselessly 0x0537")
        .that(scanner.getPropertyValue(1383))
        .isEqualTo(1335);
    assertWithMessage("Character 0x0568 matches caselessly 0x0538")
        .that(scanner.getPropertyValue(1384))
        .isEqualTo(1336);
    assertWithMessage("Character 0x0569 matches caselessly 0x0539")
        .that(scanner.getPropertyValue(1385))
        .isEqualTo(1337);
    assertWithMessage("Character 0x056a matches caselessly 0x053a")
        .that(scanner.getPropertyValue(1386))
        .isEqualTo(1338);
    assertWithMessage("Character 0x056b matches caselessly 0x053b")
        .that(scanner.getPropertyValue(1387))
        .isEqualTo(1339);
    assertWithMessage("Character 0x056c matches caselessly 0x053c")
        .that(scanner.getPropertyValue(1388))
        .isEqualTo(1340);
    assertWithMessage("Character 0x056d matches caselessly 0x053d")
        .that(scanner.getPropertyValue(1389))
        .isEqualTo(1341);
    assertWithMessage("Character 0x056e matches caselessly 0x053e")
        .that(scanner.getPropertyValue(1390))
        .isEqualTo(1342);
    assertWithMessage("Character 0x056f matches caselessly 0x053f")
        .that(scanner.getPropertyValue(1391))
        .isEqualTo(1343);
    assertWithMessage("Character 0x0570 matches caselessly 0x0540")
        .that(scanner.getPropertyValue(1392))
        .isEqualTo(1344);
    assertWithMessage("Character 0x0571 matches caselessly 0x0541")
        .that(scanner.getPropertyValue(1393))
        .isEqualTo(1345);
    assertWithMessage("Character 0x0572 matches caselessly 0x0542")
        .that(scanner.getPropertyValue(1394))
        .isEqualTo(1346);
    assertWithMessage("Character 0x0573 matches caselessly 0x0543")
        .that(scanner.getPropertyValue(1395))
        .isEqualTo(1347);
    assertWithMessage("Character 0x0574 matches caselessly 0x0544")
        .that(scanner.getPropertyValue(1396))
        .isEqualTo(1348);
    assertWithMessage("Character 0x0575 matches caselessly 0x0545")
        .that(scanner.getPropertyValue(1397))
        .isEqualTo(1349);
    assertWithMessage("Character 0x0576 matches caselessly 0x0546")
        .that(scanner.getPropertyValue(1398))
        .isEqualTo(1350);
    assertWithMessage("Character 0x0577 matches caselessly 0x0547")
        .that(scanner.getPropertyValue(1399))
        .isEqualTo(1351);
    assertWithMessage("Character 0x0578 matches caselessly 0x0548")
        .that(scanner.getPropertyValue(1400))
        .isEqualTo(1352);
    assertWithMessage("Character 0x0579 matches caselessly 0x0549")
        .that(scanner.getPropertyValue(1401))
        .isEqualTo(1353);
    assertWithMessage("Character 0x057a matches caselessly 0x054a")
        .that(scanner.getPropertyValue(1402))
        .isEqualTo(1354);
    assertWithMessage("Character 0x057b matches caselessly 0x054b")
        .that(scanner.getPropertyValue(1403))
        .isEqualTo(1355);
    assertWithMessage("Character 0x057c matches caselessly 0x054c")
        .that(scanner.getPropertyValue(1404))
        .isEqualTo(1356);
    assertWithMessage("Character 0x057d matches caselessly 0x054d")
        .that(scanner.getPropertyValue(1405))
        .isEqualTo(1357);
    assertWithMessage("Character 0x057e matches caselessly 0x054e")
        .that(scanner.getPropertyValue(1406))
        .isEqualTo(1358);
    assertWithMessage("Character 0x057f matches caselessly 0x054f")
        .that(scanner.getPropertyValue(1407))
        .isEqualTo(1359);
    assertWithMessage("Character 0x0580 matches caselessly 0x0550")
        .that(scanner.getPropertyValue(1408))
        .isEqualTo(1360);
    assertWithMessage("Character 0x0581 matches caselessly 0x0551")
        .that(scanner.getPropertyValue(1409))
        .isEqualTo(1361);
    assertWithMessage("Character 0x0582 matches caselessly 0x0552")
        .that(scanner.getPropertyValue(1410))
        .isEqualTo(1362);
    assertWithMessage("Character 0x0583 matches caselessly 0x0553")
        .that(scanner.getPropertyValue(1411))
        .isEqualTo(1363);
    assertWithMessage("Character 0x0584 matches caselessly 0x0554")
        .that(scanner.getPropertyValue(1412))
        .isEqualTo(1364);
    assertWithMessage("Character 0x0585 matches caselessly 0x0555")
        .that(scanner.getPropertyValue(1413))
        .isEqualTo(1365);
    assertWithMessage("Character 0x0586 matches caselessly 0x0556")
        .that(scanner.getPropertyValue(1414))
        .isEqualTo(1366);
    assertWithMessage("Character 0x10a0 matches caselessly 0x10a0")
        .that(scanner.getPropertyValue(4256))
        .isEqualTo(4256);
    assertWithMessage("Character 0x10a1 matches caselessly 0x10a1")
        .that(scanner.getPropertyValue(4257))
        .isEqualTo(4257);
    assertWithMessage("Character 0x10a2 matches caselessly 0x10a2")
        .that(scanner.getPropertyValue(4258))
        .isEqualTo(4258);
    assertWithMessage("Character 0x10a3 matches caselessly 0x10a3")
        .that(scanner.getPropertyValue(4259))
        .isEqualTo(4259);
    assertWithMessage("Character 0x10a4 matches caselessly 0x10a4")
        .that(scanner.getPropertyValue(4260))
        .isEqualTo(4260);
    assertWithMessage("Character 0x10a5 matches caselessly 0x10a5")
        .that(scanner.getPropertyValue(4261))
        .isEqualTo(4261);
    assertWithMessage("Character 0x10a6 matches caselessly 0x10a6")
        .that(scanner.getPropertyValue(4262))
        .isEqualTo(4262);
    assertWithMessage("Character 0x10a7 matches caselessly 0x10a7")
        .that(scanner.getPropertyValue(4263))
        .isEqualTo(4263);
    assertWithMessage("Character 0x10a8 matches caselessly 0x10a8")
        .that(scanner.getPropertyValue(4264))
        .isEqualTo(4264);
    assertWithMessage("Character 0x10a9 matches caselessly 0x10a9")
        .that(scanner.getPropertyValue(4265))
        .isEqualTo(4265);
    assertWithMessage("Character 0x10aa matches caselessly 0x10aa")
        .that(scanner.getPropertyValue(4266))
        .isEqualTo(4266);
    assertWithMessage("Character 0x10ab matches caselessly 0x10ab")
        .that(scanner.getPropertyValue(4267))
        .isEqualTo(4267);
    assertWithMessage("Character 0x10ac matches caselessly 0x10ac")
        .that(scanner.getPropertyValue(4268))
        .isEqualTo(4268);
    assertWithMessage("Character 0x10ad matches caselessly 0x10ad")
        .that(scanner.getPropertyValue(4269))
        .isEqualTo(4269);
    assertWithMessage("Character 0x10ae matches caselessly 0x10ae")
        .that(scanner.getPropertyValue(4270))
        .isEqualTo(4270);
    assertWithMessage("Character 0x10af matches caselessly 0x10af")
        .that(scanner.getPropertyValue(4271))
        .isEqualTo(4271);
    assertWithMessage("Character 0x10b0 matches caselessly 0x10b0")
        .that(scanner.getPropertyValue(4272))
        .isEqualTo(4272);
    assertWithMessage("Character 0x10b1 matches caselessly 0x10b1")
        .that(scanner.getPropertyValue(4273))
        .isEqualTo(4273);
    assertWithMessage("Character 0x10b2 matches caselessly 0x10b2")
        .that(scanner.getPropertyValue(4274))
        .isEqualTo(4274);
    assertWithMessage("Character 0x10b3 matches caselessly 0x10b3")
        .that(scanner.getPropertyValue(4275))
        .isEqualTo(4275);
    assertWithMessage("Character 0x10b4 matches caselessly 0x10b4")
        .that(scanner.getPropertyValue(4276))
        .isEqualTo(4276);
    assertWithMessage("Character 0x10b5 matches caselessly 0x10b5")
        .that(scanner.getPropertyValue(4277))
        .isEqualTo(4277);
    assertWithMessage("Character 0x10b6 matches caselessly 0x10b6")
        .that(scanner.getPropertyValue(4278))
        .isEqualTo(4278);
    assertWithMessage("Character 0x10b7 matches caselessly 0x10b7")
        .that(scanner.getPropertyValue(4279))
        .isEqualTo(4279);
    assertWithMessage("Character 0x10b8 matches caselessly 0x10b8")
        .that(scanner.getPropertyValue(4280))
        .isEqualTo(4280);
    assertWithMessage("Character 0x10b9 matches caselessly 0x10b9")
        .that(scanner.getPropertyValue(4281))
        .isEqualTo(4281);
    assertWithMessage("Character 0x10ba matches caselessly 0x10ba")
        .that(scanner.getPropertyValue(4282))
        .isEqualTo(4282);
    assertWithMessage("Character 0x10bb matches caselessly 0x10bb")
        .that(scanner.getPropertyValue(4283))
        .isEqualTo(4283);
    assertWithMessage("Character 0x10bc matches caselessly 0x10bc")
        .that(scanner.getPropertyValue(4284))
        .isEqualTo(4284);
    assertWithMessage("Character 0x10bd matches caselessly 0x10bd")
        .that(scanner.getPropertyValue(4285))
        .isEqualTo(4285);
    assertWithMessage("Character 0x10be matches caselessly 0x10be")
        .that(scanner.getPropertyValue(4286))
        .isEqualTo(4286);
    assertWithMessage("Character 0x10bf matches caselessly 0x10bf")
        .that(scanner.getPropertyValue(4287))
        .isEqualTo(4287);
    assertWithMessage("Character 0x10c0 matches caselessly 0x10c0")
        .that(scanner.getPropertyValue(4288))
        .isEqualTo(4288);
    assertWithMessage("Character 0x10c1 matches caselessly 0x10c1")
        .that(scanner.getPropertyValue(4289))
        .isEqualTo(4289);
    assertWithMessage("Character 0x10c2 matches caselessly 0x10c2")
        .that(scanner.getPropertyValue(4290))
        .isEqualTo(4290);
    assertWithMessage("Character 0x10c3 matches caselessly 0x10c3")
        .that(scanner.getPropertyValue(4291))
        .isEqualTo(4291);
    assertWithMessage("Character 0x10c4 matches caselessly 0x10c4")
        .that(scanner.getPropertyValue(4292))
        .isEqualTo(4292);
    assertWithMessage("Character 0x10c5 matches caselessly 0x10c5")
        .that(scanner.getPropertyValue(4293))
        .isEqualTo(4293);
    assertWithMessage("Character 0x1e00 matches caselessly 0x1e00")
        .that(scanner.getPropertyValue(7680))
        .isEqualTo(7680);
    assertWithMessage("Character 0x1e01 matches caselessly 0x1e00")
        .that(scanner.getPropertyValue(7681))
        .isEqualTo(7680);
    assertWithMessage("Character 0x1e02 matches caselessly 0x1e02")
        .that(scanner.getPropertyValue(7682))
        .isEqualTo(7682);
    assertWithMessage("Character 0x1e03 matches caselessly 0x1e02")
        .that(scanner.getPropertyValue(7683))
        .isEqualTo(7682);
    assertWithMessage("Character 0x1e04 matches caselessly 0x1e04")
        .that(scanner.getPropertyValue(7684))
        .isEqualTo(7684);
    assertWithMessage("Character 0x1e05 matches caselessly 0x1e04")
        .that(scanner.getPropertyValue(7685))
        .isEqualTo(7684);
    assertWithMessage("Character 0x1e06 matches caselessly 0x1e06")
        .that(scanner.getPropertyValue(7686))
        .isEqualTo(7686);
    assertWithMessage("Character 0x1e07 matches caselessly 0x1e06")
        .that(scanner.getPropertyValue(7687))
        .isEqualTo(7686);
    assertWithMessage("Character 0x1e08 matches caselessly 0x1e08")
        .that(scanner.getPropertyValue(7688))
        .isEqualTo(7688);
    assertWithMessage("Character 0x1e09 matches caselessly 0x1e08")
        .that(scanner.getPropertyValue(7689))
        .isEqualTo(7688);
    assertWithMessage("Character 0x1e0a matches caselessly 0x1e0a")
        .that(scanner.getPropertyValue(7690))
        .isEqualTo(7690);
    assertWithMessage("Character 0x1e0b matches caselessly 0x1e0a")
        .that(scanner.getPropertyValue(7691))
        .isEqualTo(7690);
    assertWithMessage("Character 0x1e0c matches caselessly 0x1e0c")
        .that(scanner.getPropertyValue(7692))
        .isEqualTo(7692);
    assertWithMessage("Character 0x1e0d matches caselessly 0x1e0c")
        .that(scanner.getPropertyValue(7693))
        .isEqualTo(7692);
    assertWithMessage("Character 0x1e0e matches caselessly 0x1e0e")
        .that(scanner.getPropertyValue(7694))
        .isEqualTo(7694);
    assertWithMessage("Character 0x1e0f matches caselessly 0x1e0e")
        .that(scanner.getPropertyValue(7695))
        .isEqualTo(7694);
    assertWithMessage("Character 0x1e10 matches caselessly 0x1e10")
        .that(scanner.getPropertyValue(7696))
        .isEqualTo(7696);
    assertWithMessage("Character 0x1e11 matches caselessly 0x1e10")
        .that(scanner.getPropertyValue(7697))
        .isEqualTo(7696);
    assertWithMessage("Character 0x1e12 matches caselessly 0x1e12")
        .that(scanner.getPropertyValue(7698))
        .isEqualTo(7698);
    assertWithMessage("Character 0x1e13 matches caselessly 0x1e12")
        .that(scanner.getPropertyValue(7699))
        .isEqualTo(7698);
    assertWithMessage("Character 0x1e14 matches caselessly 0x1e14")
        .that(scanner.getPropertyValue(7700))
        .isEqualTo(7700);
    assertWithMessage("Character 0x1e15 matches caselessly 0x1e14")
        .that(scanner.getPropertyValue(7701))
        .isEqualTo(7700);
    assertWithMessage("Character 0x1e16 matches caselessly 0x1e16")
        .that(scanner.getPropertyValue(7702))
        .isEqualTo(7702);
    assertWithMessage("Character 0x1e17 matches caselessly 0x1e16")
        .that(scanner.getPropertyValue(7703))
        .isEqualTo(7702);
    assertWithMessage("Character 0x1e18 matches caselessly 0x1e18")
        .that(scanner.getPropertyValue(7704))
        .isEqualTo(7704);
    assertWithMessage("Character 0x1e19 matches caselessly 0x1e18")
        .that(scanner.getPropertyValue(7705))
        .isEqualTo(7704);
    assertWithMessage("Character 0x1e1a matches caselessly 0x1e1a")
        .that(scanner.getPropertyValue(7706))
        .isEqualTo(7706);
    assertWithMessage("Character 0x1e1b matches caselessly 0x1e1a")
        .that(scanner.getPropertyValue(7707))
        .isEqualTo(7706);
    assertWithMessage("Character 0x1e1c matches caselessly 0x1e1c")
        .that(scanner.getPropertyValue(7708))
        .isEqualTo(7708);
    assertWithMessage("Character 0x1e1d matches caselessly 0x1e1c")
        .that(scanner.getPropertyValue(7709))
        .isEqualTo(7708);
    assertWithMessage("Character 0x1e1e matches caselessly 0x1e1e")
        .that(scanner.getPropertyValue(7710))
        .isEqualTo(7710);
    assertWithMessage("Character 0x1e1f matches caselessly 0x1e1e")
        .that(scanner.getPropertyValue(7711))
        .isEqualTo(7710);
    assertWithMessage("Character 0x1e20 matches caselessly 0x1e20")
        .that(scanner.getPropertyValue(7712))
        .isEqualTo(7712);
    assertWithMessage("Character 0x1e21 matches caselessly 0x1e20")
        .that(scanner.getPropertyValue(7713))
        .isEqualTo(7712);
    assertWithMessage("Character 0x1e22 matches caselessly 0x1e22")
        .that(scanner.getPropertyValue(7714))
        .isEqualTo(7714);
    assertWithMessage("Character 0x1e23 matches caselessly 0x1e22")
        .that(scanner.getPropertyValue(7715))
        .isEqualTo(7714);
    assertWithMessage("Character 0x1e24 matches caselessly 0x1e24")
        .that(scanner.getPropertyValue(7716))
        .isEqualTo(7716);
    assertWithMessage("Character 0x1e25 matches caselessly 0x1e24")
        .that(scanner.getPropertyValue(7717))
        .isEqualTo(7716);
    assertWithMessage("Character 0x1e26 matches caselessly 0x1e26")
        .that(scanner.getPropertyValue(7718))
        .isEqualTo(7718);
    assertWithMessage("Character 0x1e27 matches caselessly 0x1e26")
        .that(scanner.getPropertyValue(7719))
        .isEqualTo(7718);
    assertWithMessage("Character 0x1e28 matches caselessly 0x1e28")
        .that(scanner.getPropertyValue(7720))
        .isEqualTo(7720);
    assertWithMessage("Character 0x1e29 matches caselessly 0x1e28")
        .that(scanner.getPropertyValue(7721))
        .isEqualTo(7720);
    assertWithMessage("Character 0x1e2a matches caselessly 0x1e2a")
        .that(scanner.getPropertyValue(7722))
        .isEqualTo(7722);
    assertWithMessage("Character 0x1e2b matches caselessly 0x1e2a")
        .that(scanner.getPropertyValue(7723))
        .isEqualTo(7722);
    assertWithMessage("Character 0x1e2c matches caselessly 0x1e2c")
        .that(scanner.getPropertyValue(7724))
        .isEqualTo(7724);
    assertWithMessage("Character 0x1e2d matches caselessly 0x1e2c")
        .that(scanner.getPropertyValue(7725))
        .isEqualTo(7724);
    assertWithMessage("Character 0x1e2e matches caselessly 0x1e2e")
        .that(scanner.getPropertyValue(7726))
        .isEqualTo(7726);
    assertWithMessage("Character 0x1e2f matches caselessly 0x1e2e")
        .that(scanner.getPropertyValue(7727))
        .isEqualTo(7726);
    assertWithMessage("Character 0x1e30 matches caselessly 0x1e30")
        .that(scanner.getPropertyValue(7728))
        .isEqualTo(7728);
    assertWithMessage("Character 0x1e31 matches caselessly 0x1e30")
        .that(scanner.getPropertyValue(7729))
        .isEqualTo(7728);
    assertWithMessage("Character 0x1e32 matches caselessly 0x1e32")
        .that(scanner.getPropertyValue(7730))
        .isEqualTo(7730);
    assertWithMessage("Character 0x1e33 matches caselessly 0x1e32")
        .that(scanner.getPropertyValue(7731))
        .isEqualTo(7730);
    assertWithMessage("Character 0x1e34 matches caselessly 0x1e34")
        .that(scanner.getPropertyValue(7732))
        .isEqualTo(7732);
    assertWithMessage("Character 0x1e35 matches caselessly 0x1e34")
        .that(scanner.getPropertyValue(7733))
        .isEqualTo(7732);
    assertWithMessage("Character 0x1e36 matches caselessly 0x1e36")
        .that(scanner.getPropertyValue(7734))
        .isEqualTo(7734);
    assertWithMessage("Character 0x1e37 matches caselessly 0x1e36")
        .that(scanner.getPropertyValue(7735))
        .isEqualTo(7734);
    assertWithMessage("Character 0x1e38 matches caselessly 0x1e38")
        .that(scanner.getPropertyValue(7736))
        .isEqualTo(7736);
    assertWithMessage("Character 0x1e39 matches caselessly 0x1e38")
        .that(scanner.getPropertyValue(7737))
        .isEqualTo(7736);
    assertWithMessage("Character 0x1e3a matches caselessly 0x1e3a")
        .that(scanner.getPropertyValue(7738))
        .isEqualTo(7738);
    assertWithMessage("Character 0x1e3b matches caselessly 0x1e3a")
        .that(scanner.getPropertyValue(7739))
        .isEqualTo(7738);
    assertWithMessage("Character 0x1e3c matches caselessly 0x1e3c")
        .that(scanner.getPropertyValue(7740))
        .isEqualTo(7740);
    assertWithMessage("Character 0x1e3d matches caselessly 0x1e3c")
        .that(scanner.getPropertyValue(7741))
        .isEqualTo(7740);
    assertWithMessage("Character 0x1e3e matches caselessly 0x1e3e")
        .that(scanner.getPropertyValue(7742))
        .isEqualTo(7742);
    assertWithMessage("Character 0x1e3f matches caselessly 0x1e3e")
        .that(scanner.getPropertyValue(7743))
        .isEqualTo(7742);
    assertWithMessage("Character 0x1e40 matches caselessly 0x1e40")
        .that(scanner.getPropertyValue(7744))
        .isEqualTo(7744);
    assertWithMessage("Character 0x1e41 matches caselessly 0x1e40")
        .that(scanner.getPropertyValue(7745))
        .isEqualTo(7744);
    assertWithMessage("Character 0x1e42 matches caselessly 0x1e42")
        .that(scanner.getPropertyValue(7746))
        .isEqualTo(7746);
    assertWithMessage("Character 0x1e43 matches caselessly 0x1e42")
        .that(scanner.getPropertyValue(7747))
        .isEqualTo(7746);
    assertWithMessage("Character 0x1e44 matches caselessly 0x1e44")
        .that(scanner.getPropertyValue(7748))
        .isEqualTo(7748);
    assertWithMessage("Character 0x1e45 matches caselessly 0x1e44")
        .that(scanner.getPropertyValue(7749))
        .isEqualTo(7748);
    assertWithMessage("Character 0x1e46 matches caselessly 0x1e46")
        .that(scanner.getPropertyValue(7750))
        .isEqualTo(7750);
    assertWithMessage("Character 0x1e47 matches caselessly 0x1e46")
        .that(scanner.getPropertyValue(7751))
        .isEqualTo(7750);
    assertWithMessage("Character 0x1e48 matches caselessly 0x1e48")
        .that(scanner.getPropertyValue(7752))
        .isEqualTo(7752);
    assertWithMessage("Character 0x1e49 matches caselessly 0x1e48")
        .that(scanner.getPropertyValue(7753))
        .isEqualTo(7752);
    assertWithMessage("Character 0x1e4a matches caselessly 0x1e4a")
        .that(scanner.getPropertyValue(7754))
        .isEqualTo(7754);
    assertWithMessage("Character 0x1e4b matches caselessly 0x1e4a")
        .that(scanner.getPropertyValue(7755))
        .isEqualTo(7754);
    assertWithMessage("Character 0x1e4c matches caselessly 0x1e4c")
        .that(scanner.getPropertyValue(7756))
        .isEqualTo(7756);
    assertWithMessage("Character 0x1e4d matches caselessly 0x1e4c")
        .that(scanner.getPropertyValue(7757))
        .isEqualTo(7756);
    assertWithMessage("Character 0x1e4e matches caselessly 0x1e4e")
        .that(scanner.getPropertyValue(7758))
        .isEqualTo(7758);
    assertWithMessage("Character 0x1e4f matches caselessly 0x1e4e")
        .that(scanner.getPropertyValue(7759))
        .isEqualTo(7758);
    assertWithMessage("Character 0x1e50 matches caselessly 0x1e50")
        .that(scanner.getPropertyValue(7760))
        .isEqualTo(7760);
    assertWithMessage("Character 0x1e51 matches caselessly 0x1e50")
        .that(scanner.getPropertyValue(7761))
        .isEqualTo(7760);
    assertWithMessage("Character 0x1e52 matches caselessly 0x1e52")
        .that(scanner.getPropertyValue(7762))
        .isEqualTo(7762);
    assertWithMessage("Character 0x1e53 matches caselessly 0x1e52")
        .that(scanner.getPropertyValue(7763))
        .isEqualTo(7762);
    assertWithMessage("Character 0x1e54 matches caselessly 0x1e54")
        .that(scanner.getPropertyValue(7764))
        .isEqualTo(7764);
    assertWithMessage("Character 0x1e55 matches caselessly 0x1e54")
        .that(scanner.getPropertyValue(7765))
        .isEqualTo(7764);
    assertWithMessage("Character 0x1e56 matches caselessly 0x1e56")
        .that(scanner.getPropertyValue(7766))
        .isEqualTo(7766);
    assertWithMessage("Character 0x1e57 matches caselessly 0x1e56")
        .that(scanner.getPropertyValue(7767))
        .isEqualTo(7766);
    assertWithMessage("Character 0x1e58 matches caselessly 0x1e58")
        .that(scanner.getPropertyValue(7768))
        .isEqualTo(7768);
    assertWithMessage("Character 0x1e59 matches caselessly 0x1e58")
        .that(scanner.getPropertyValue(7769))
        .isEqualTo(7768);
    assertWithMessage("Character 0x1e5a matches caselessly 0x1e5a")
        .that(scanner.getPropertyValue(7770))
        .isEqualTo(7770);
    assertWithMessage("Character 0x1e5b matches caselessly 0x1e5a")
        .that(scanner.getPropertyValue(7771))
        .isEqualTo(7770);
    assertWithMessage("Character 0x1e5c matches caselessly 0x1e5c")
        .that(scanner.getPropertyValue(7772))
        .isEqualTo(7772);
    assertWithMessage("Character 0x1e5d matches caselessly 0x1e5c")
        .that(scanner.getPropertyValue(7773))
        .isEqualTo(7772);
    assertWithMessage("Character 0x1e5e matches caselessly 0x1e5e")
        .that(scanner.getPropertyValue(7774))
        .isEqualTo(7774);
    assertWithMessage("Character 0x1e5f matches caselessly 0x1e5e")
        .that(scanner.getPropertyValue(7775))
        .isEqualTo(7774);
    assertWithMessage("Character 0x1e60 matches caselessly 0x1e60")
        .that(scanner.getPropertyValue(7776))
        .isEqualTo(7776);
    assertWithMessage("Character 0x1e61 matches caselessly 0x1e60")
        .that(scanner.getPropertyValue(7777))
        .isEqualTo(7776);
    assertWithMessage("Character 0x1e62 matches caselessly 0x1e62")
        .that(scanner.getPropertyValue(7778))
        .isEqualTo(7778);
    assertWithMessage("Character 0x1e63 matches caselessly 0x1e62")
        .that(scanner.getPropertyValue(7779))
        .isEqualTo(7778);
    assertWithMessage("Character 0x1e64 matches caselessly 0x1e64")
        .that(scanner.getPropertyValue(7780))
        .isEqualTo(7780);
    assertWithMessage("Character 0x1e65 matches caselessly 0x1e64")
        .that(scanner.getPropertyValue(7781))
        .isEqualTo(7780);
    assertWithMessage("Character 0x1e66 matches caselessly 0x1e66")
        .that(scanner.getPropertyValue(7782))
        .isEqualTo(7782);
    assertWithMessage("Character 0x1e67 matches caselessly 0x1e66")
        .that(scanner.getPropertyValue(7783))
        .isEqualTo(7782);
    assertWithMessage("Character 0x1e68 matches caselessly 0x1e68")
        .that(scanner.getPropertyValue(7784))
        .isEqualTo(7784);
    assertWithMessage("Character 0x1e69 matches caselessly 0x1e68")
        .that(scanner.getPropertyValue(7785))
        .isEqualTo(7784);
    assertWithMessage("Character 0x1e6a matches caselessly 0x1e6a")
        .that(scanner.getPropertyValue(7786))
        .isEqualTo(7786);
    assertWithMessage("Character 0x1e6b matches caselessly 0x1e6a")
        .that(scanner.getPropertyValue(7787))
        .isEqualTo(7786);
    assertWithMessage("Character 0x1e6c matches caselessly 0x1e6c")
        .that(scanner.getPropertyValue(7788))
        .isEqualTo(7788);
    assertWithMessage("Character 0x1e6d matches caselessly 0x1e6c")
        .that(scanner.getPropertyValue(7789))
        .isEqualTo(7788);
    assertWithMessage("Character 0x1e6e matches caselessly 0x1e6e")
        .that(scanner.getPropertyValue(7790))
        .isEqualTo(7790);
    assertWithMessage("Character 0x1e6f matches caselessly 0x1e6e")
        .that(scanner.getPropertyValue(7791))
        .isEqualTo(7790);
    assertWithMessage("Character 0x1e70 matches caselessly 0x1e70")
        .that(scanner.getPropertyValue(7792))
        .isEqualTo(7792);
    assertWithMessage("Character 0x1e71 matches caselessly 0x1e70")
        .that(scanner.getPropertyValue(7793))
        .isEqualTo(7792);
    assertWithMessage("Character 0x1e72 matches caselessly 0x1e72")
        .that(scanner.getPropertyValue(7794))
        .isEqualTo(7794);
    assertWithMessage("Character 0x1e73 matches caselessly 0x1e72")
        .that(scanner.getPropertyValue(7795))
        .isEqualTo(7794);
    assertWithMessage("Character 0x1e74 matches caselessly 0x1e74")
        .that(scanner.getPropertyValue(7796))
        .isEqualTo(7796);
    assertWithMessage("Character 0x1e75 matches caselessly 0x1e74")
        .that(scanner.getPropertyValue(7797))
        .isEqualTo(7796);
    assertWithMessage("Character 0x1e76 matches caselessly 0x1e76")
        .that(scanner.getPropertyValue(7798))
        .isEqualTo(7798);
    assertWithMessage("Character 0x1e77 matches caselessly 0x1e76")
        .that(scanner.getPropertyValue(7799))
        .isEqualTo(7798);
    assertWithMessage("Character 0x1e78 matches caselessly 0x1e78")
        .that(scanner.getPropertyValue(7800))
        .isEqualTo(7800);
    assertWithMessage("Character 0x1e79 matches caselessly 0x1e78")
        .that(scanner.getPropertyValue(7801))
        .isEqualTo(7800);
    assertWithMessage("Character 0x1e7a matches caselessly 0x1e7a")
        .that(scanner.getPropertyValue(7802))
        .isEqualTo(7802);
    assertWithMessage("Character 0x1e7b matches caselessly 0x1e7a")
        .that(scanner.getPropertyValue(7803))
        .isEqualTo(7802);
    assertWithMessage("Character 0x1e7c matches caselessly 0x1e7c")
        .that(scanner.getPropertyValue(7804))
        .isEqualTo(7804);
    assertWithMessage("Character 0x1e7d matches caselessly 0x1e7c")
        .that(scanner.getPropertyValue(7805))
        .isEqualTo(7804);
    assertWithMessage("Character 0x1e7e matches caselessly 0x1e7e")
        .that(scanner.getPropertyValue(7806))
        .isEqualTo(7806);
    assertWithMessage("Character 0x1e7f matches caselessly 0x1e7e")
        .that(scanner.getPropertyValue(7807))
        .isEqualTo(7806);
    assertWithMessage("Character 0x1e80 matches caselessly 0x1e80")
        .that(scanner.getPropertyValue(7808))
        .isEqualTo(7808);
    assertWithMessage("Character 0x1e81 matches caselessly 0x1e80")
        .that(scanner.getPropertyValue(7809))
        .isEqualTo(7808);
    assertWithMessage("Character 0x1e82 matches caselessly 0x1e82")
        .that(scanner.getPropertyValue(7810))
        .isEqualTo(7810);
    assertWithMessage("Character 0x1e83 matches caselessly 0x1e82")
        .that(scanner.getPropertyValue(7811))
        .isEqualTo(7810);
    assertWithMessage("Character 0x1e84 matches caselessly 0x1e84")
        .that(scanner.getPropertyValue(7812))
        .isEqualTo(7812);
    assertWithMessage("Character 0x1e85 matches caselessly 0x1e84")
        .that(scanner.getPropertyValue(7813))
        .isEqualTo(7812);
    assertWithMessage("Character 0x1e86 matches caselessly 0x1e86")
        .that(scanner.getPropertyValue(7814))
        .isEqualTo(7814);
    assertWithMessage("Character 0x1e87 matches caselessly 0x1e86")
        .that(scanner.getPropertyValue(7815))
        .isEqualTo(7814);
    assertWithMessage("Character 0x1e88 matches caselessly 0x1e88")
        .that(scanner.getPropertyValue(7816))
        .isEqualTo(7816);
    assertWithMessage("Character 0x1e89 matches caselessly 0x1e88")
        .that(scanner.getPropertyValue(7817))
        .isEqualTo(7816);
    assertWithMessage("Character 0x1e8a matches caselessly 0x1e8a")
        .that(scanner.getPropertyValue(7818))
        .isEqualTo(7818);
    assertWithMessage("Character 0x1e8b matches caselessly 0x1e8a")
        .that(scanner.getPropertyValue(7819))
        .isEqualTo(7818);
    assertWithMessage("Character 0x1e8c matches caselessly 0x1e8c")
        .that(scanner.getPropertyValue(7820))
        .isEqualTo(7820);
    assertWithMessage("Character 0x1e8d matches caselessly 0x1e8c")
        .that(scanner.getPropertyValue(7821))
        .isEqualTo(7820);
    assertWithMessage("Character 0x1e8e matches caselessly 0x1e8e")
        .that(scanner.getPropertyValue(7822))
        .isEqualTo(7822);
    assertWithMessage("Character 0x1e8f matches caselessly 0x1e8e")
        .that(scanner.getPropertyValue(7823))
        .isEqualTo(7822);
    assertWithMessage("Character 0x1e90 matches caselessly 0x1e90")
        .that(scanner.getPropertyValue(7824))
        .isEqualTo(7824);
    assertWithMessage("Character 0x1e91 matches caselessly 0x1e90")
        .that(scanner.getPropertyValue(7825))
        .isEqualTo(7824);
    assertWithMessage("Character 0x1e92 matches caselessly 0x1e92")
        .that(scanner.getPropertyValue(7826))
        .isEqualTo(7826);
    assertWithMessage("Character 0x1e93 matches caselessly 0x1e92")
        .that(scanner.getPropertyValue(7827))
        .isEqualTo(7826);
    assertWithMessage("Character 0x1e94 matches caselessly 0x1e94")
        .that(scanner.getPropertyValue(7828))
        .isEqualTo(7828);
    assertWithMessage("Character 0x1e95 matches caselessly 0x1e94")
        .that(scanner.getPropertyValue(7829))
        .isEqualTo(7828);
    assertWithMessage("Character 0x1e9b matches caselessly 0x1e60")
        .that(scanner.getPropertyValue(7835))
        .isEqualTo(7776);
    assertWithMessage("Character 0x1ea0 matches caselessly 0x1ea0")
        .that(scanner.getPropertyValue(7840))
        .isEqualTo(7840);
    assertWithMessage("Character 0x1ea1 matches caselessly 0x1ea0")
        .that(scanner.getPropertyValue(7841))
        .isEqualTo(7840);
    assertWithMessage("Character 0x1ea2 matches caselessly 0x1ea2")
        .that(scanner.getPropertyValue(7842))
        .isEqualTo(7842);
    assertWithMessage("Character 0x1ea3 matches caselessly 0x1ea2")
        .that(scanner.getPropertyValue(7843))
        .isEqualTo(7842);
    assertWithMessage("Character 0x1ea4 matches caselessly 0x1ea4")
        .that(scanner.getPropertyValue(7844))
        .isEqualTo(7844);
    assertWithMessage("Character 0x1ea5 matches caselessly 0x1ea4")
        .that(scanner.getPropertyValue(7845))
        .isEqualTo(7844);
    assertWithMessage("Character 0x1ea6 matches caselessly 0x1ea6")
        .that(scanner.getPropertyValue(7846))
        .isEqualTo(7846);
    assertWithMessage("Character 0x1ea7 matches caselessly 0x1ea6")
        .that(scanner.getPropertyValue(7847))
        .isEqualTo(7846);
    assertWithMessage("Character 0x1ea8 matches caselessly 0x1ea8")
        .that(scanner.getPropertyValue(7848))
        .isEqualTo(7848);
    assertWithMessage("Character 0x1ea9 matches caselessly 0x1ea8")
        .that(scanner.getPropertyValue(7849))
        .isEqualTo(7848);
    assertWithMessage("Character 0x1eaa matches caselessly 0x1eaa")
        .that(scanner.getPropertyValue(7850))
        .isEqualTo(7850);
    assertWithMessage("Character 0x1eab matches caselessly 0x1eaa")
        .that(scanner.getPropertyValue(7851))
        .isEqualTo(7850);
    assertWithMessage("Character 0x1eac matches caselessly 0x1eac")
        .that(scanner.getPropertyValue(7852))
        .isEqualTo(7852);
    assertWithMessage("Character 0x1ead matches caselessly 0x1eac")
        .that(scanner.getPropertyValue(7853))
        .isEqualTo(7852);
    assertWithMessage("Character 0x1eae matches caselessly 0x1eae")
        .that(scanner.getPropertyValue(7854))
        .isEqualTo(7854);
    assertWithMessage("Character 0x1eaf matches caselessly 0x1eae")
        .that(scanner.getPropertyValue(7855))
        .isEqualTo(7854);
    assertWithMessage("Character 0x1eb0 matches caselessly 0x1eb0")
        .that(scanner.getPropertyValue(7856))
        .isEqualTo(7856);
    assertWithMessage("Character 0x1eb1 matches caselessly 0x1eb0")
        .that(scanner.getPropertyValue(7857))
        .isEqualTo(7856);
    assertWithMessage("Character 0x1eb2 matches caselessly 0x1eb2")
        .that(scanner.getPropertyValue(7858))
        .isEqualTo(7858);
    assertWithMessage("Character 0x1eb3 matches caselessly 0x1eb2")
        .that(scanner.getPropertyValue(7859))
        .isEqualTo(7858);
    assertWithMessage("Character 0x1eb4 matches caselessly 0x1eb4")
        .that(scanner.getPropertyValue(7860))
        .isEqualTo(7860);
    assertWithMessage("Character 0x1eb5 matches caselessly 0x1eb4")
        .that(scanner.getPropertyValue(7861))
        .isEqualTo(7860);
    assertWithMessage("Character 0x1eb6 matches caselessly 0x1eb6")
        .that(scanner.getPropertyValue(7862))
        .isEqualTo(7862);
    assertWithMessage("Character 0x1eb7 matches caselessly 0x1eb6")
        .that(scanner.getPropertyValue(7863))
        .isEqualTo(7862);
    assertWithMessage("Character 0x1eb8 matches caselessly 0x1eb8")
        .that(scanner.getPropertyValue(7864))
        .isEqualTo(7864);
    assertWithMessage("Character 0x1eb9 matches caselessly 0x1eb8")
        .that(scanner.getPropertyValue(7865))
        .isEqualTo(7864);
    assertWithMessage("Character 0x1eba matches caselessly 0x1eba")
        .that(scanner.getPropertyValue(7866))
        .isEqualTo(7866);
    assertWithMessage("Character 0x1ebb matches caselessly 0x1eba")
        .that(scanner.getPropertyValue(7867))
        .isEqualTo(7866);
    assertWithMessage("Character 0x1ebc matches caselessly 0x1ebc")
        .that(scanner.getPropertyValue(7868))
        .isEqualTo(7868);
    assertWithMessage("Character 0x1ebd matches caselessly 0x1ebc")
        .that(scanner.getPropertyValue(7869))
        .isEqualTo(7868);
    assertWithMessage("Character 0x1ebe matches caselessly 0x1ebe")
        .that(scanner.getPropertyValue(7870))
        .isEqualTo(7870);
    assertWithMessage("Character 0x1ebf matches caselessly 0x1ebe")
        .that(scanner.getPropertyValue(7871))
        .isEqualTo(7870);
    assertWithMessage("Character 0x1ec0 matches caselessly 0x1ec0")
        .that(scanner.getPropertyValue(7872))
        .isEqualTo(7872);
    assertWithMessage("Character 0x1ec1 matches caselessly 0x1ec0")
        .that(scanner.getPropertyValue(7873))
        .isEqualTo(7872);
    assertWithMessage("Character 0x1ec2 matches caselessly 0x1ec2")
        .that(scanner.getPropertyValue(7874))
        .isEqualTo(7874);
    assertWithMessage("Character 0x1ec3 matches caselessly 0x1ec2")
        .that(scanner.getPropertyValue(7875))
        .isEqualTo(7874);
    assertWithMessage("Character 0x1ec4 matches caselessly 0x1ec4")
        .that(scanner.getPropertyValue(7876))
        .isEqualTo(7876);
    assertWithMessage("Character 0x1ec5 matches caselessly 0x1ec4")
        .that(scanner.getPropertyValue(7877))
        .isEqualTo(7876);
    assertWithMessage("Character 0x1ec6 matches caselessly 0x1ec6")
        .that(scanner.getPropertyValue(7878))
        .isEqualTo(7878);
    assertWithMessage("Character 0x1ec7 matches caselessly 0x1ec6")
        .that(scanner.getPropertyValue(7879))
        .isEqualTo(7878);
    assertWithMessage("Character 0x1ec8 matches caselessly 0x1ec8")
        .that(scanner.getPropertyValue(7880))
        .isEqualTo(7880);
    assertWithMessage("Character 0x1ec9 matches caselessly 0x1ec8")
        .that(scanner.getPropertyValue(7881))
        .isEqualTo(7880);
    assertWithMessage("Character 0x1eca matches caselessly 0x1eca")
        .that(scanner.getPropertyValue(7882))
        .isEqualTo(7882);
    assertWithMessage("Character 0x1ecb matches caselessly 0x1eca")
        .that(scanner.getPropertyValue(7883))
        .isEqualTo(7882);
    assertWithMessage("Character 0x1ecc matches caselessly 0x1ecc")
        .that(scanner.getPropertyValue(7884))
        .isEqualTo(7884);
    assertWithMessage("Character 0x1ecd matches caselessly 0x1ecc")
        .that(scanner.getPropertyValue(7885))
        .isEqualTo(7884);
    assertWithMessage("Character 0x1ece matches caselessly 0x1ece")
        .that(scanner.getPropertyValue(7886))
        .isEqualTo(7886);
    assertWithMessage("Character 0x1ecf matches caselessly 0x1ece")
        .that(scanner.getPropertyValue(7887))
        .isEqualTo(7886);
    assertWithMessage("Character 0x1ed0 matches caselessly 0x1ed0")
        .that(scanner.getPropertyValue(7888))
        .isEqualTo(7888);
    assertWithMessage("Character 0x1ed1 matches caselessly 0x1ed0")
        .that(scanner.getPropertyValue(7889))
        .isEqualTo(7888);
    assertWithMessage("Character 0x1ed2 matches caselessly 0x1ed2")
        .that(scanner.getPropertyValue(7890))
        .isEqualTo(7890);
    assertWithMessage("Character 0x1ed3 matches caselessly 0x1ed2")
        .that(scanner.getPropertyValue(7891))
        .isEqualTo(7890);
    assertWithMessage("Character 0x1ed4 matches caselessly 0x1ed4")
        .that(scanner.getPropertyValue(7892))
        .isEqualTo(7892);
    assertWithMessage("Character 0x1ed5 matches caselessly 0x1ed4")
        .that(scanner.getPropertyValue(7893))
        .isEqualTo(7892);
    assertWithMessage("Character 0x1ed6 matches caselessly 0x1ed6")
        .that(scanner.getPropertyValue(7894))
        .isEqualTo(7894);
    assertWithMessage("Character 0x1ed7 matches caselessly 0x1ed6")
        .that(scanner.getPropertyValue(7895))
        .isEqualTo(7894);
    assertWithMessage("Character 0x1ed8 matches caselessly 0x1ed8")
        .that(scanner.getPropertyValue(7896))
        .isEqualTo(7896);
    assertWithMessage("Character 0x1ed9 matches caselessly 0x1ed8")
        .that(scanner.getPropertyValue(7897))
        .isEqualTo(7896);
    assertWithMessage("Character 0x1eda matches caselessly 0x1eda")
        .that(scanner.getPropertyValue(7898))
        .isEqualTo(7898);
    assertWithMessage("Character 0x1edb matches caselessly 0x1eda")
        .that(scanner.getPropertyValue(7899))
        .isEqualTo(7898);
    assertWithMessage("Character 0x1edc matches caselessly 0x1edc")
        .that(scanner.getPropertyValue(7900))
        .isEqualTo(7900);
    assertWithMessage("Character 0x1edd matches caselessly 0x1edc")
        .that(scanner.getPropertyValue(7901))
        .isEqualTo(7900);
    assertWithMessage("Character 0x1ede matches caselessly 0x1ede")
        .that(scanner.getPropertyValue(7902))
        .isEqualTo(7902);
    assertWithMessage("Character 0x1edf matches caselessly 0x1ede")
        .that(scanner.getPropertyValue(7903))
        .isEqualTo(7902);
    assertWithMessage("Character 0x1ee0 matches caselessly 0x1ee0")
        .that(scanner.getPropertyValue(7904))
        .isEqualTo(7904);
    assertWithMessage("Character 0x1ee1 matches caselessly 0x1ee0")
        .that(scanner.getPropertyValue(7905))
        .isEqualTo(7904);
    assertWithMessage("Character 0x1ee2 matches caselessly 0x1ee2")
        .that(scanner.getPropertyValue(7906))
        .isEqualTo(7906);
    assertWithMessage("Character 0x1ee3 matches caselessly 0x1ee2")
        .that(scanner.getPropertyValue(7907))
        .isEqualTo(7906);
    assertWithMessage("Character 0x1ee4 matches caselessly 0x1ee4")
        .that(scanner.getPropertyValue(7908))
        .isEqualTo(7908);
    assertWithMessage("Character 0x1ee5 matches caselessly 0x1ee4")
        .that(scanner.getPropertyValue(7909))
        .isEqualTo(7908);
    assertWithMessage("Character 0x1ee6 matches caselessly 0x1ee6")
        .that(scanner.getPropertyValue(7910))
        .isEqualTo(7910);
    assertWithMessage("Character 0x1ee7 matches caselessly 0x1ee6")
        .that(scanner.getPropertyValue(7911))
        .isEqualTo(7910);
    assertWithMessage("Character 0x1ee8 matches caselessly 0x1ee8")
        .that(scanner.getPropertyValue(7912))
        .isEqualTo(7912);
    assertWithMessage("Character 0x1ee9 matches caselessly 0x1ee8")
        .that(scanner.getPropertyValue(7913))
        .isEqualTo(7912);
    assertWithMessage("Character 0x1eea matches caselessly 0x1eea")
        .that(scanner.getPropertyValue(7914))
        .isEqualTo(7914);
    assertWithMessage("Character 0x1eeb matches caselessly 0x1eea")
        .that(scanner.getPropertyValue(7915))
        .isEqualTo(7914);
    assertWithMessage("Character 0x1eec matches caselessly 0x1eec")
        .that(scanner.getPropertyValue(7916))
        .isEqualTo(7916);
    assertWithMessage("Character 0x1eed matches caselessly 0x1eec")
        .that(scanner.getPropertyValue(7917))
        .isEqualTo(7916);
    assertWithMessage("Character 0x1eee matches caselessly 0x1eee")
        .that(scanner.getPropertyValue(7918))
        .isEqualTo(7918);
    assertWithMessage("Character 0x1eef matches caselessly 0x1eee")
        .that(scanner.getPropertyValue(7919))
        .isEqualTo(7918);
    assertWithMessage("Character 0x1ef0 matches caselessly 0x1ef0")
        .that(scanner.getPropertyValue(7920))
        .isEqualTo(7920);
    assertWithMessage("Character 0x1ef1 matches caselessly 0x1ef0")
        .that(scanner.getPropertyValue(7921))
        .isEqualTo(7920);
    assertWithMessage("Character 0x1ef2 matches caselessly 0x1ef2")
        .that(scanner.getPropertyValue(7922))
        .isEqualTo(7922);
    assertWithMessage("Character 0x1ef3 matches caselessly 0x1ef2")
        .that(scanner.getPropertyValue(7923))
        .isEqualTo(7922);
    assertWithMessage("Character 0x1ef4 matches caselessly 0x1ef4")
        .that(scanner.getPropertyValue(7924))
        .isEqualTo(7924);
    assertWithMessage("Character 0x1ef5 matches caselessly 0x1ef4")
        .that(scanner.getPropertyValue(7925))
        .isEqualTo(7924);
    assertWithMessage("Character 0x1ef6 matches caselessly 0x1ef6")
        .that(scanner.getPropertyValue(7926))
        .isEqualTo(7926);
    assertWithMessage("Character 0x1ef7 matches caselessly 0x1ef6")
        .that(scanner.getPropertyValue(7927))
        .isEqualTo(7926);
    assertWithMessage("Character 0x1ef8 matches caselessly 0x1ef8")
        .that(scanner.getPropertyValue(7928))
        .isEqualTo(7928);
    assertWithMessage("Character 0x1ef9 matches caselessly 0x1ef8")
        .that(scanner.getPropertyValue(7929))
        .isEqualTo(7928);
    assertWithMessage("Character 0x1f00 matches caselessly 0x1f00")
        .that(scanner.getPropertyValue(7936))
        .isEqualTo(7936);
    assertWithMessage("Character 0x1f01 matches caselessly 0x1f01")
        .that(scanner.getPropertyValue(7937))
        .isEqualTo(7937);
    assertWithMessage("Character 0x1f02 matches caselessly 0x1f02")
        .that(scanner.getPropertyValue(7938))
        .isEqualTo(7938);
    assertWithMessage("Character 0x1f03 matches caselessly 0x1f03")
        .that(scanner.getPropertyValue(7939))
        .isEqualTo(7939);
    assertWithMessage("Character 0x1f04 matches caselessly 0x1f04")
        .that(scanner.getPropertyValue(7940))
        .isEqualTo(7940);
    assertWithMessage("Character 0x1f05 matches caselessly 0x1f05")
        .that(scanner.getPropertyValue(7941))
        .isEqualTo(7941);
    assertWithMessage("Character 0x1f06 matches caselessly 0x1f06")
        .that(scanner.getPropertyValue(7942))
        .isEqualTo(7942);
    assertWithMessage("Character 0x1f07 matches caselessly 0x1f07")
        .that(scanner.getPropertyValue(7943))
        .isEqualTo(7943);
    assertWithMessage("Character 0x1f08 matches caselessly 0x1f00")
        .that(scanner.getPropertyValue(7944))
        .isEqualTo(7936);
    assertWithMessage("Character 0x1f09 matches caselessly 0x1f01")
        .that(scanner.getPropertyValue(7945))
        .isEqualTo(7937);
    assertWithMessage("Character 0x1f0a matches caselessly 0x1f02")
        .that(scanner.getPropertyValue(7946))
        .isEqualTo(7938);
    assertWithMessage("Character 0x1f0b matches caselessly 0x1f03")
        .that(scanner.getPropertyValue(7947))
        .isEqualTo(7939);
    assertWithMessage("Character 0x1f0c matches caselessly 0x1f04")
        .that(scanner.getPropertyValue(7948))
        .isEqualTo(7940);
    assertWithMessage("Character 0x1f0d matches caselessly 0x1f05")
        .that(scanner.getPropertyValue(7949))
        .isEqualTo(7941);
    assertWithMessage("Character 0x1f0e matches caselessly 0x1f06")
        .that(scanner.getPropertyValue(7950))
        .isEqualTo(7942);
    assertWithMessage("Character 0x1f0f matches caselessly 0x1f07")
        .that(scanner.getPropertyValue(7951))
        .isEqualTo(7943);
    assertWithMessage("Character 0x1f10 matches caselessly 0x1f10")
        .that(scanner.getPropertyValue(7952))
        .isEqualTo(7952);
    assertWithMessage("Character 0x1f11 matches caselessly 0x1f11")
        .that(scanner.getPropertyValue(7953))
        .isEqualTo(7953);
    assertWithMessage("Character 0x1f12 matches caselessly 0x1f12")
        .that(scanner.getPropertyValue(7954))
        .isEqualTo(7954);
    assertWithMessage("Character 0x1f13 matches caselessly 0x1f13")
        .that(scanner.getPropertyValue(7955))
        .isEqualTo(7955);
    assertWithMessage("Character 0x1f14 matches caselessly 0x1f14")
        .that(scanner.getPropertyValue(7956))
        .isEqualTo(7956);
    assertWithMessage("Character 0x1f15 matches caselessly 0x1f15")
        .that(scanner.getPropertyValue(7957))
        .isEqualTo(7957);
    assertWithMessage("Character 0x1f18 matches caselessly 0x1f10")
        .that(scanner.getPropertyValue(7960))
        .isEqualTo(7952);
    assertWithMessage("Character 0x1f19 matches caselessly 0x1f11")
        .that(scanner.getPropertyValue(7961))
        .isEqualTo(7953);
    assertWithMessage("Character 0x1f1a matches caselessly 0x1f12")
        .that(scanner.getPropertyValue(7962))
        .isEqualTo(7954);
    assertWithMessage("Character 0x1f1b matches caselessly 0x1f13")
        .that(scanner.getPropertyValue(7963))
        .isEqualTo(7955);
    assertWithMessage("Character 0x1f1c matches caselessly 0x1f14")
        .that(scanner.getPropertyValue(7964))
        .isEqualTo(7956);
    assertWithMessage("Character 0x1f1d matches caselessly 0x1f15")
        .that(scanner.getPropertyValue(7965))
        .isEqualTo(7957);
    assertWithMessage("Character 0x1f20 matches caselessly 0x1f20")
        .that(scanner.getPropertyValue(7968))
        .isEqualTo(7968);
    assertWithMessage("Character 0x1f21 matches caselessly 0x1f21")
        .that(scanner.getPropertyValue(7969))
        .isEqualTo(7969);
    assertWithMessage("Character 0x1f22 matches caselessly 0x1f22")
        .that(scanner.getPropertyValue(7970))
        .isEqualTo(7970);
    assertWithMessage("Character 0x1f23 matches caselessly 0x1f23")
        .that(scanner.getPropertyValue(7971))
        .isEqualTo(7971);
    assertWithMessage("Character 0x1f24 matches caselessly 0x1f24")
        .that(scanner.getPropertyValue(7972))
        .isEqualTo(7972);
    assertWithMessage("Character 0x1f25 matches caselessly 0x1f25")
        .that(scanner.getPropertyValue(7973))
        .isEqualTo(7973);
    assertWithMessage("Character 0x1f26 matches caselessly 0x1f26")
        .that(scanner.getPropertyValue(7974))
        .isEqualTo(7974);
    assertWithMessage("Character 0x1f27 matches caselessly 0x1f27")
        .that(scanner.getPropertyValue(7975))
        .isEqualTo(7975);
    assertWithMessage("Character 0x1f28 matches caselessly 0x1f20")
        .that(scanner.getPropertyValue(7976))
        .isEqualTo(7968);
    assertWithMessage("Character 0x1f29 matches caselessly 0x1f21")
        .that(scanner.getPropertyValue(7977))
        .isEqualTo(7969);
    assertWithMessage("Character 0x1f2a matches caselessly 0x1f22")
        .that(scanner.getPropertyValue(7978))
        .isEqualTo(7970);
    assertWithMessage("Character 0x1f2b matches caselessly 0x1f23")
        .that(scanner.getPropertyValue(7979))
        .isEqualTo(7971);
    assertWithMessage("Character 0x1f2c matches caselessly 0x1f24")
        .that(scanner.getPropertyValue(7980))
        .isEqualTo(7972);
    assertWithMessage("Character 0x1f2d matches caselessly 0x1f25")
        .that(scanner.getPropertyValue(7981))
        .isEqualTo(7973);
    assertWithMessage("Character 0x1f2e matches caselessly 0x1f26")
        .that(scanner.getPropertyValue(7982))
        .isEqualTo(7974);
    assertWithMessage("Character 0x1f2f matches caselessly 0x1f27")
        .that(scanner.getPropertyValue(7983))
        .isEqualTo(7975);
    assertWithMessage("Character 0x1f30 matches caselessly 0x1f30")
        .that(scanner.getPropertyValue(7984))
        .isEqualTo(7984);
    assertWithMessage("Character 0x1f31 matches caselessly 0x1f31")
        .that(scanner.getPropertyValue(7985))
        .isEqualTo(7985);
    assertWithMessage("Character 0x1f32 matches caselessly 0x1f32")
        .that(scanner.getPropertyValue(7986))
        .isEqualTo(7986);
    assertWithMessage("Character 0x1f33 matches caselessly 0x1f33")
        .that(scanner.getPropertyValue(7987))
        .isEqualTo(7987);
    assertWithMessage("Character 0x1f34 matches caselessly 0x1f34")
        .that(scanner.getPropertyValue(7988))
        .isEqualTo(7988);
    assertWithMessage("Character 0x1f35 matches caselessly 0x1f35")
        .that(scanner.getPropertyValue(7989))
        .isEqualTo(7989);
    assertWithMessage("Character 0x1f36 matches caselessly 0x1f36")
        .that(scanner.getPropertyValue(7990))
        .isEqualTo(7990);
    assertWithMessage("Character 0x1f37 matches caselessly 0x1f37")
        .that(scanner.getPropertyValue(7991))
        .isEqualTo(7991);
    assertWithMessage("Character 0x1f38 matches caselessly 0x1f30")
        .that(scanner.getPropertyValue(7992))
        .isEqualTo(7984);
    assertWithMessage("Character 0x1f39 matches caselessly 0x1f31")
        .that(scanner.getPropertyValue(7993))
        .isEqualTo(7985);
    assertWithMessage("Character 0x1f3a matches caselessly 0x1f32")
        .that(scanner.getPropertyValue(7994))
        .isEqualTo(7986);
    assertWithMessage("Character 0x1f3b matches caselessly 0x1f33")
        .that(scanner.getPropertyValue(7995))
        .isEqualTo(7987);
    assertWithMessage("Character 0x1f3c matches caselessly 0x1f34")
        .that(scanner.getPropertyValue(7996))
        .isEqualTo(7988);
    assertWithMessage("Character 0x1f3d matches caselessly 0x1f35")
        .that(scanner.getPropertyValue(7997))
        .isEqualTo(7989);
    assertWithMessage("Character 0x1f3e matches caselessly 0x1f36")
        .that(scanner.getPropertyValue(7998))
        .isEqualTo(7990);
    assertWithMessage("Character 0x1f3f matches caselessly 0x1f37")
        .that(scanner.getPropertyValue(7999))
        .isEqualTo(7991);
    assertWithMessage("Character 0x1f40 matches caselessly 0x1f40")
        .that(scanner.getPropertyValue(8000))
        .isEqualTo(8000);
    assertWithMessage("Character 0x1f41 matches caselessly 0x1f41")
        .that(scanner.getPropertyValue(8001))
        .isEqualTo(8001);
    assertWithMessage("Character 0x1f42 matches caselessly 0x1f42")
        .that(scanner.getPropertyValue(8002))
        .isEqualTo(8002);
    assertWithMessage("Character 0x1f43 matches caselessly 0x1f43")
        .that(scanner.getPropertyValue(8003))
        .isEqualTo(8003);
    assertWithMessage("Character 0x1f44 matches caselessly 0x1f44")
        .that(scanner.getPropertyValue(8004))
        .isEqualTo(8004);
    assertWithMessage("Character 0x1f45 matches caselessly 0x1f45")
        .that(scanner.getPropertyValue(8005))
        .isEqualTo(8005);
    assertWithMessage("Character 0x1f48 matches caselessly 0x1f40")
        .that(scanner.getPropertyValue(8008))
        .isEqualTo(8000);
    assertWithMessage("Character 0x1f49 matches caselessly 0x1f41")
        .that(scanner.getPropertyValue(8009))
        .isEqualTo(8001);
    assertWithMessage("Character 0x1f4a matches caselessly 0x1f42")
        .that(scanner.getPropertyValue(8010))
        .isEqualTo(8002);
    assertWithMessage("Character 0x1f4b matches caselessly 0x1f43")
        .that(scanner.getPropertyValue(8011))
        .isEqualTo(8003);
    assertWithMessage("Character 0x1f4c matches caselessly 0x1f44")
        .that(scanner.getPropertyValue(8012))
        .isEqualTo(8004);
    assertWithMessage("Character 0x1f4d matches caselessly 0x1f45")
        .that(scanner.getPropertyValue(8013))
        .isEqualTo(8005);
    assertWithMessage("Character 0x1f51 matches caselessly 0x1f51")
        .that(scanner.getPropertyValue(8017))
        .isEqualTo(8017);
    assertWithMessage("Character 0x1f53 matches caselessly 0x1f53")
        .that(scanner.getPropertyValue(8019))
        .isEqualTo(8019);
    assertWithMessage("Character 0x1f55 matches caselessly 0x1f55")
        .that(scanner.getPropertyValue(8021))
        .isEqualTo(8021);
    assertWithMessage("Character 0x1f57 matches caselessly 0x1f57")
        .that(scanner.getPropertyValue(8023))
        .isEqualTo(8023);
    assertWithMessage("Character 0x1f59 matches caselessly 0x1f51")
        .that(scanner.getPropertyValue(8025))
        .isEqualTo(8017);
    assertWithMessage("Character 0x1f5b matches caselessly 0x1f53")
        .that(scanner.getPropertyValue(8027))
        .isEqualTo(8019);
    assertWithMessage("Character 0x1f5d matches caselessly 0x1f55")
        .that(scanner.getPropertyValue(8029))
        .isEqualTo(8021);
    assertWithMessage("Character 0x1f5f matches caselessly 0x1f57")
        .that(scanner.getPropertyValue(8031))
        .isEqualTo(8023);
    assertWithMessage("Character 0x1f60 matches caselessly 0x1f60")
        .that(scanner.getPropertyValue(8032))
        .isEqualTo(8032);
    assertWithMessage("Character 0x1f61 matches caselessly 0x1f61")
        .that(scanner.getPropertyValue(8033))
        .isEqualTo(8033);
    assertWithMessage("Character 0x1f62 matches caselessly 0x1f62")
        .that(scanner.getPropertyValue(8034))
        .isEqualTo(8034);
    assertWithMessage("Character 0x1f63 matches caselessly 0x1f63")
        .that(scanner.getPropertyValue(8035))
        .isEqualTo(8035);
    assertWithMessage("Character 0x1f64 matches caselessly 0x1f64")
        .that(scanner.getPropertyValue(8036))
        .isEqualTo(8036);
    assertWithMessage("Character 0x1f65 matches caselessly 0x1f65")
        .that(scanner.getPropertyValue(8037))
        .isEqualTo(8037);
    assertWithMessage("Character 0x1f66 matches caselessly 0x1f66")
        .that(scanner.getPropertyValue(8038))
        .isEqualTo(8038);
    assertWithMessage("Character 0x1f67 matches caselessly 0x1f67")
        .that(scanner.getPropertyValue(8039))
        .isEqualTo(8039);
    assertWithMessage("Character 0x1f68 matches caselessly 0x1f60")
        .that(scanner.getPropertyValue(8040))
        .isEqualTo(8032);
    assertWithMessage("Character 0x1f69 matches caselessly 0x1f61")
        .that(scanner.getPropertyValue(8041))
        .isEqualTo(8033);
    assertWithMessage("Character 0x1f6a matches caselessly 0x1f62")
        .that(scanner.getPropertyValue(8042))
        .isEqualTo(8034);
    assertWithMessage("Character 0x1f6b matches caselessly 0x1f63")
        .that(scanner.getPropertyValue(8043))
        .isEqualTo(8035);
    assertWithMessage("Character 0x1f6c matches caselessly 0x1f64")
        .that(scanner.getPropertyValue(8044))
        .isEqualTo(8036);
    assertWithMessage("Character 0x1f6d matches caselessly 0x1f65")
        .that(scanner.getPropertyValue(8045))
        .isEqualTo(8037);
    assertWithMessage("Character 0x1f6e matches caselessly 0x1f66")
        .that(scanner.getPropertyValue(8046))
        .isEqualTo(8038);
    assertWithMessage("Character 0x1f6f matches caselessly 0x1f67")
        .that(scanner.getPropertyValue(8047))
        .isEqualTo(8039);
    assertWithMessage("Character 0x1f70 matches caselessly 0x1f70")
        .that(scanner.getPropertyValue(8048))
        .isEqualTo(8048);
    assertWithMessage("Character 0x1f71 matches caselessly 0x1f71")
        .that(scanner.getPropertyValue(8049))
        .isEqualTo(8049);
    assertWithMessage("Character 0x1f72 matches caselessly 0x1f72")
        .that(scanner.getPropertyValue(8050))
        .isEqualTo(8050);
    assertWithMessage("Character 0x1f73 matches caselessly 0x1f73")
        .that(scanner.getPropertyValue(8051))
        .isEqualTo(8051);
    assertWithMessage("Character 0x1f74 matches caselessly 0x1f74")
        .that(scanner.getPropertyValue(8052))
        .isEqualTo(8052);
    assertWithMessage("Character 0x1f75 matches caselessly 0x1f75")
        .that(scanner.getPropertyValue(8053))
        .isEqualTo(8053);
    assertWithMessage("Character 0x1f76 matches caselessly 0x1f76")
        .that(scanner.getPropertyValue(8054))
        .isEqualTo(8054);
    assertWithMessage("Character 0x1f77 matches caselessly 0x1f77")
        .that(scanner.getPropertyValue(8055))
        .isEqualTo(8055);
    assertWithMessage("Character 0x1f78 matches caselessly 0x1f78")
        .that(scanner.getPropertyValue(8056))
        .isEqualTo(8056);
    assertWithMessage("Character 0x1f79 matches caselessly 0x1f79")
        .that(scanner.getPropertyValue(8057))
        .isEqualTo(8057);
    assertWithMessage("Character 0x1f7a matches caselessly 0x1f7a")
        .that(scanner.getPropertyValue(8058))
        .isEqualTo(8058);
    assertWithMessage("Character 0x1f7b matches caselessly 0x1f7b")
        .that(scanner.getPropertyValue(8059))
        .isEqualTo(8059);
    assertWithMessage("Character 0x1f7c matches caselessly 0x1f7c")
        .that(scanner.getPropertyValue(8060))
        .isEqualTo(8060);
    assertWithMessage("Character 0x1f7d matches caselessly 0x1f7d")
        .that(scanner.getPropertyValue(8061))
        .isEqualTo(8061);
    assertWithMessage("Character 0x1f80 matches caselessly 0x1f80")
        .that(scanner.getPropertyValue(8064))
        .isEqualTo(8064);
    assertWithMessage("Character 0x1f81 matches caselessly 0x1f81")
        .that(scanner.getPropertyValue(8065))
        .isEqualTo(8065);
    assertWithMessage("Character 0x1f82 matches caselessly 0x1f82")
        .that(scanner.getPropertyValue(8066))
        .isEqualTo(8066);
    assertWithMessage("Character 0x1f83 matches caselessly 0x1f83")
        .that(scanner.getPropertyValue(8067))
        .isEqualTo(8067);
    assertWithMessage("Character 0x1f84 matches caselessly 0x1f84")
        .that(scanner.getPropertyValue(8068))
        .isEqualTo(8068);
    assertWithMessage("Character 0x1f85 matches caselessly 0x1f85")
        .that(scanner.getPropertyValue(8069))
        .isEqualTo(8069);
    assertWithMessage("Character 0x1f86 matches caselessly 0x1f86")
        .that(scanner.getPropertyValue(8070))
        .isEqualTo(8070);
    assertWithMessage("Character 0x1f87 matches caselessly 0x1f87")
        .that(scanner.getPropertyValue(8071))
        .isEqualTo(8071);
    assertWithMessage("Character 0x1f88 matches caselessly 0x1f80")
        .that(scanner.getPropertyValue(8072))
        .isEqualTo(8064);
    assertWithMessage("Character 0x1f89 matches caselessly 0x1f81")
        .that(scanner.getPropertyValue(8073))
        .isEqualTo(8065);
    assertWithMessage("Character 0x1f8a matches caselessly 0x1f82")
        .that(scanner.getPropertyValue(8074))
        .isEqualTo(8066);
    assertWithMessage("Character 0x1f8b matches caselessly 0x1f83")
        .that(scanner.getPropertyValue(8075))
        .isEqualTo(8067);
    assertWithMessage("Character 0x1f8c matches caselessly 0x1f84")
        .that(scanner.getPropertyValue(8076))
        .isEqualTo(8068);
    assertWithMessage("Character 0x1f8d matches caselessly 0x1f85")
        .that(scanner.getPropertyValue(8077))
        .isEqualTo(8069);
    assertWithMessage("Character 0x1f8e matches caselessly 0x1f86")
        .that(scanner.getPropertyValue(8078))
        .isEqualTo(8070);
    assertWithMessage("Character 0x1f8f matches caselessly 0x1f87")
        .that(scanner.getPropertyValue(8079))
        .isEqualTo(8071);
    assertWithMessage("Character 0x1f90 matches caselessly 0x1f90")
        .that(scanner.getPropertyValue(8080))
        .isEqualTo(8080);
    assertWithMessage("Character 0x1f91 matches caselessly 0x1f91")
        .that(scanner.getPropertyValue(8081))
        .isEqualTo(8081);
    assertWithMessage("Character 0x1f92 matches caselessly 0x1f92")
        .that(scanner.getPropertyValue(8082))
        .isEqualTo(8082);
    assertWithMessage("Character 0x1f93 matches caselessly 0x1f93")
        .that(scanner.getPropertyValue(8083))
        .isEqualTo(8083);
    assertWithMessage("Character 0x1f94 matches caselessly 0x1f94")
        .that(scanner.getPropertyValue(8084))
        .isEqualTo(8084);
    assertWithMessage("Character 0x1f95 matches caselessly 0x1f95")
        .that(scanner.getPropertyValue(8085))
        .isEqualTo(8085);
    assertWithMessage("Character 0x1f96 matches caselessly 0x1f96")
        .that(scanner.getPropertyValue(8086))
        .isEqualTo(8086);
    assertWithMessage("Character 0x1f97 matches caselessly 0x1f97")
        .that(scanner.getPropertyValue(8087))
        .isEqualTo(8087);
    assertWithMessage("Character 0x1f98 matches caselessly 0x1f90")
        .that(scanner.getPropertyValue(8088))
        .isEqualTo(8080);
    assertWithMessage("Character 0x1f99 matches caselessly 0x1f91")
        .that(scanner.getPropertyValue(8089))
        .isEqualTo(8081);
    assertWithMessage("Character 0x1f9a matches caselessly 0x1f92")
        .that(scanner.getPropertyValue(8090))
        .isEqualTo(8082);
    assertWithMessage("Character 0x1f9b matches caselessly 0x1f93")
        .that(scanner.getPropertyValue(8091))
        .isEqualTo(8083);
    assertWithMessage("Character 0x1f9c matches caselessly 0x1f94")
        .that(scanner.getPropertyValue(8092))
        .isEqualTo(8084);
    assertWithMessage("Character 0x1f9d matches caselessly 0x1f95")
        .that(scanner.getPropertyValue(8093))
        .isEqualTo(8085);
    assertWithMessage("Character 0x1f9e matches caselessly 0x1f96")
        .that(scanner.getPropertyValue(8094))
        .isEqualTo(8086);
    assertWithMessage("Character 0x1f9f matches caselessly 0x1f97")
        .that(scanner.getPropertyValue(8095))
        .isEqualTo(8087);
    assertWithMessage("Character 0x1fa0 matches caselessly 0x1fa0")
        .that(scanner.getPropertyValue(8096))
        .isEqualTo(8096);
    assertWithMessage("Character 0x1fa1 matches caselessly 0x1fa1")
        .that(scanner.getPropertyValue(8097))
        .isEqualTo(8097);
    assertWithMessage("Character 0x1fa2 matches caselessly 0x1fa2")
        .that(scanner.getPropertyValue(8098))
        .isEqualTo(8098);
    assertWithMessage("Character 0x1fa3 matches caselessly 0x1fa3")
        .that(scanner.getPropertyValue(8099))
        .isEqualTo(8099);
    assertWithMessage("Character 0x1fa4 matches caselessly 0x1fa4")
        .that(scanner.getPropertyValue(8100))
        .isEqualTo(8100);
    assertWithMessage("Character 0x1fa5 matches caselessly 0x1fa5")
        .that(scanner.getPropertyValue(8101))
        .isEqualTo(8101);
    assertWithMessage("Character 0x1fa6 matches caselessly 0x1fa6")
        .that(scanner.getPropertyValue(8102))
        .isEqualTo(8102);
    assertWithMessage("Character 0x1fa7 matches caselessly 0x1fa7")
        .that(scanner.getPropertyValue(8103))
        .isEqualTo(8103);
    assertWithMessage("Character 0x1fa8 matches caselessly 0x1fa0")
        .that(scanner.getPropertyValue(8104))
        .isEqualTo(8096);
    assertWithMessage("Character 0x1fa9 matches caselessly 0x1fa1")
        .that(scanner.getPropertyValue(8105))
        .isEqualTo(8097);
    assertWithMessage("Character 0x1faa matches caselessly 0x1fa2")
        .that(scanner.getPropertyValue(8106))
        .isEqualTo(8098);
    assertWithMessage("Character 0x1fab matches caselessly 0x1fa3")
        .that(scanner.getPropertyValue(8107))
        .isEqualTo(8099);
    assertWithMessage("Character 0x1fac matches caselessly 0x1fa4")
        .that(scanner.getPropertyValue(8108))
        .isEqualTo(8100);
    assertWithMessage("Character 0x1fad matches caselessly 0x1fa5")
        .that(scanner.getPropertyValue(8109))
        .isEqualTo(8101);
    assertWithMessage("Character 0x1fae matches caselessly 0x1fa6")
        .that(scanner.getPropertyValue(8110))
        .isEqualTo(8102);
    assertWithMessage("Character 0x1faf matches caselessly 0x1fa7")
        .that(scanner.getPropertyValue(8111))
        .isEqualTo(8103);
    assertWithMessage("Character 0x1fb0 matches caselessly 0x1fb0")
        .that(scanner.getPropertyValue(8112))
        .isEqualTo(8112);
    assertWithMessage("Character 0x1fb1 matches caselessly 0x1fb1")
        .that(scanner.getPropertyValue(8113))
        .isEqualTo(8113);
    assertWithMessage("Character 0x1fb3 matches caselessly 0x1fb3")
        .that(scanner.getPropertyValue(8115))
        .isEqualTo(8115);
    assertWithMessage("Character 0x1fb8 matches caselessly 0x1fb0")
        .that(scanner.getPropertyValue(8120))
        .isEqualTo(8112);
    assertWithMessage("Character 0x1fb9 matches caselessly 0x1fb1")
        .that(scanner.getPropertyValue(8121))
        .isEqualTo(8113);
    assertWithMessage("Character 0x1fba matches caselessly 0x1f70")
        .that(scanner.getPropertyValue(8122))
        .isEqualTo(8048);
    assertWithMessage("Character 0x1fbb matches caselessly 0x1f71")
        .that(scanner.getPropertyValue(8123))
        .isEqualTo(8049);
    assertWithMessage("Character 0x1fbc matches caselessly 0x1fb3")
        .that(scanner.getPropertyValue(8124))
        .isEqualTo(8115);
    assertWithMessage("Character 0x1fbe matches caselessly 0x0345")
        .that(scanner.getPropertyValue(8126))
        .isEqualTo(837);
    assertWithMessage("Character 0x1fc3 matches caselessly 0x1fc3")
        .that(scanner.getPropertyValue(8131))
        .isEqualTo(8131);
    assertWithMessage("Character 0x1fc8 matches caselessly 0x1f72")
        .that(scanner.getPropertyValue(8136))
        .isEqualTo(8050);
    assertWithMessage("Character 0x1fc9 matches caselessly 0x1f73")
        .that(scanner.getPropertyValue(8137))
        .isEqualTo(8051);
    assertWithMessage("Character 0x1fca matches caselessly 0x1f74")
        .that(scanner.getPropertyValue(8138))
        .isEqualTo(8052);
    assertWithMessage("Character 0x1fcb matches caselessly 0x1f75")
        .that(scanner.getPropertyValue(8139))
        .isEqualTo(8053);
    assertWithMessage("Character 0x1fcc matches caselessly 0x1fc3")
        .that(scanner.getPropertyValue(8140))
        .isEqualTo(8131);
    assertWithMessage("Character 0x1fd0 matches caselessly 0x1fd0")
        .that(scanner.getPropertyValue(8144))
        .isEqualTo(8144);
    assertWithMessage("Character 0x1fd1 matches caselessly 0x1fd1")
        .that(scanner.getPropertyValue(8145))
        .isEqualTo(8145);
    assertWithMessage("Character 0x1fd8 matches caselessly 0x1fd0")
        .that(scanner.getPropertyValue(8152))
        .isEqualTo(8144);
    assertWithMessage("Character 0x1fd9 matches caselessly 0x1fd1")
        .that(scanner.getPropertyValue(8153))
        .isEqualTo(8145);
    assertWithMessage("Character 0x1fda matches caselessly 0x1f76")
        .that(scanner.getPropertyValue(8154))
        .isEqualTo(8054);
    assertWithMessage("Character 0x1fdb matches caselessly 0x1f77")
        .that(scanner.getPropertyValue(8155))
        .isEqualTo(8055);
    assertWithMessage("Character 0x1fe0 matches caselessly 0x1fe0")
        .that(scanner.getPropertyValue(8160))
        .isEqualTo(8160);
    assertWithMessage("Character 0x1fe1 matches caselessly 0x1fe1")
        .that(scanner.getPropertyValue(8161))
        .isEqualTo(8161);
    assertWithMessage("Character 0x1fe5 matches caselessly 0x1fe5")
        .that(scanner.getPropertyValue(8165))
        .isEqualTo(8165);
    assertWithMessage("Character 0x1fe8 matches caselessly 0x1fe0")
        .that(scanner.getPropertyValue(8168))
        .isEqualTo(8160);
    assertWithMessage("Character 0x1fe9 matches caselessly 0x1fe1")
        .that(scanner.getPropertyValue(8169))
        .isEqualTo(8161);
    assertWithMessage("Character 0x1fea matches caselessly 0x1f7a")
        .that(scanner.getPropertyValue(8170))
        .isEqualTo(8058);
    assertWithMessage("Character 0x1feb matches caselessly 0x1f7b")
        .that(scanner.getPropertyValue(8171))
        .isEqualTo(8059);
    assertWithMessage("Character 0x1fec matches caselessly 0x1fe5")
        .that(scanner.getPropertyValue(8172))
        .isEqualTo(8165);
    assertWithMessage("Character 0x1ff3 matches caselessly 0x1ff3")
        .that(scanner.getPropertyValue(8179))
        .isEqualTo(8179);
    assertWithMessage("Character 0x1ff8 matches caselessly 0x1f78")
        .that(scanner.getPropertyValue(8184))
        .isEqualTo(8056);
    assertWithMessage("Character 0x1ff9 matches caselessly 0x1f79")
        .that(scanner.getPropertyValue(8185))
        .isEqualTo(8057);
    assertWithMessage("Character 0x1ffa matches caselessly 0x1f7c")
        .that(scanner.getPropertyValue(8186))
        .isEqualTo(8060);
    assertWithMessage("Character 0x1ffb matches caselessly 0x1f7d")
        .that(scanner.getPropertyValue(8187))
        .isEqualTo(8061);
    assertWithMessage("Character 0x1ffc matches caselessly 0x1ff3")
        .that(scanner.getPropertyValue(8188))
        .isEqualTo(8179);
    assertWithMessage("Character 0x2126 matches caselessly 0x03a9")
        .that(scanner.getPropertyValue(8486))
        .isEqualTo(937);
    assertWithMessage("Character 0x212a matches caselessly 0x004b")
        .that(scanner.getPropertyValue(8490))
        .isEqualTo(75);
    assertWithMessage("Character 0x212b matches caselessly 0x00c5")
        .that(scanner.getPropertyValue(8491))
        .isEqualTo(197);
    assertWithMessage("Character 0x2160 matches caselessly 0x2160")
        .that(scanner.getPropertyValue(8544))
        .isEqualTo(8544);
    assertWithMessage("Character 0x2161 matches caselessly 0x2161")
        .that(scanner.getPropertyValue(8545))
        .isEqualTo(8545);
    assertWithMessage("Character 0x2162 matches caselessly 0x2162")
        .that(scanner.getPropertyValue(8546))
        .isEqualTo(8546);
    assertWithMessage("Character 0x2163 matches caselessly 0x2163")
        .that(scanner.getPropertyValue(8547))
        .isEqualTo(8547);
    assertWithMessage("Character 0x2164 matches caselessly 0x2164")
        .that(scanner.getPropertyValue(8548))
        .isEqualTo(8548);
    assertWithMessage("Character 0x2165 matches caselessly 0x2165")
        .that(scanner.getPropertyValue(8549))
        .isEqualTo(8549);
    assertWithMessage("Character 0x2166 matches caselessly 0x2166")
        .that(scanner.getPropertyValue(8550))
        .isEqualTo(8550);
    assertWithMessage("Character 0x2167 matches caselessly 0x2167")
        .that(scanner.getPropertyValue(8551))
        .isEqualTo(8551);
    assertWithMessage("Character 0x2168 matches caselessly 0x2168")
        .that(scanner.getPropertyValue(8552))
        .isEqualTo(8552);
    assertWithMessage("Character 0x2169 matches caselessly 0x2169")
        .that(scanner.getPropertyValue(8553))
        .isEqualTo(8553);
    assertWithMessage("Character 0x216a matches caselessly 0x216a")
        .that(scanner.getPropertyValue(8554))
        .isEqualTo(8554);
    assertWithMessage("Character 0x216b matches caselessly 0x216b")
        .that(scanner.getPropertyValue(8555))
        .isEqualTo(8555);
    assertWithMessage("Character 0x216c matches caselessly 0x216c")
        .that(scanner.getPropertyValue(8556))
        .isEqualTo(8556);
    assertWithMessage("Character 0x216d matches caselessly 0x216d")
        .that(scanner.getPropertyValue(8557))
        .isEqualTo(8557);
    assertWithMessage("Character 0x216e matches caselessly 0x216e")
        .that(scanner.getPropertyValue(8558))
        .isEqualTo(8558);
    assertWithMessage("Character 0x216f matches caselessly 0x216f")
        .that(scanner.getPropertyValue(8559))
        .isEqualTo(8559);
    assertWithMessage("Character 0x2170 matches caselessly 0x2160")
        .that(scanner.getPropertyValue(8560))
        .isEqualTo(8544);
    assertWithMessage("Character 0x2171 matches caselessly 0x2161")
        .that(scanner.getPropertyValue(8561))
        .isEqualTo(8545);
    assertWithMessage("Character 0x2172 matches caselessly 0x2162")
        .that(scanner.getPropertyValue(8562))
        .isEqualTo(8546);
    assertWithMessage("Character 0x2173 matches caselessly 0x2163")
        .that(scanner.getPropertyValue(8563))
        .isEqualTo(8547);
    assertWithMessage("Character 0x2174 matches caselessly 0x2164")
        .that(scanner.getPropertyValue(8564))
        .isEqualTo(8548);
    assertWithMessage("Character 0x2175 matches caselessly 0x2165")
        .that(scanner.getPropertyValue(8565))
        .isEqualTo(8549);
    assertWithMessage("Character 0x2176 matches caselessly 0x2166")
        .that(scanner.getPropertyValue(8566))
        .isEqualTo(8550);
    assertWithMessage("Character 0x2177 matches caselessly 0x2167")
        .that(scanner.getPropertyValue(8567))
        .isEqualTo(8551);
    assertWithMessage("Character 0x2178 matches caselessly 0x2168")
        .that(scanner.getPropertyValue(8568))
        .isEqualTo(8552);
    assertWithMessage("Character 0x2179 matches caselessly 0x2169")
        .that(scanner.getPropertyValue(8569))
        .isEqualTo(8553);
    assertWithMessage("Character 0x217a matches caselessly 0x216a")
        .that(scanner.getPropertyValue(8570))
        .isEqualTo(8554);
    assertWithMessage("Character 0x217b matches caselessly 0x216b")
        .that(scanner.getPropertyValue(8571))
        .isEqualTo(8555);
    assertWithMessage("Character 0x217c matches caselessly 0x216c")
        .that(scanner.getPropertyValue(8572))
        .isEqualTo(8556);
    assertWithMessage("Character 0x217d matches caselessly 0x216d")
        .that(scanner.getPropertyValue(8573))
        .isEqualTo(8557);
    assertWithMessage("Character 0x217e matches caselessly 0x216e")
        .that(scanner.getPropertyValue(8574))
        .isEqualTo(8558);
    assertWithMessage("Character 0x217f matches caselessly 0x216f")
        .that(scanner.getPropertyValue(8575))
        .isEqualTo(8559);
    assertWithMessage("Character 0x24b6 matches caselessly 0x24b6")
        .that(scanner.getPropertyValue(9398))
        .isEqualTo(9398);
    assertWithMessage("Character 0x24b7 matches caselessly 0x24b7")
        .that(scanner.getPropertyValue(9399))
        .isEqualTo(9399);
    assertWithMessage("Character 0x24b8 matches caselessly 0x24b8")
        .that(scanner.getPropertyValue(9400))
        .isEqualTo(9400);
    assertWithMessage("Character 0x24b9 matches caselessly 0x24b9")
        .that(scanner.getPropertyValue(9401))
        .isEqualTo(9401);
    assertWithMessage("Character 0x24ba matches caselessly 0x24ba")
        .that(scanner.getPropertyValue(9402))
        .isEqualTo(9402);
    assertWithMessage("Character 0x24bb matches caselessly 0x24bb")
        .that(scanner.getPropertyValue(9403))
        .isEqualTo(9403);
    assertWithMessage("Character 0x24bc matches caselessly 0x24bc")
        .that(scanner.getPropertyValue(9404))
        .isEqualTo(9404);
    assertWithMessage("Character 0x24bd matches caselessly 0x24bd")
        .that(scanner.getPropertyValue(9405))
        .isEqualTo(9405);
    assertWithMessage("Character 0x24be matches caselessly 0x24be")
        .that(scanner.getPropertyValue(9406))
        .isEqualTo(9406);
    assertWithMessage("Character 0x24bf matches caselessly 0x24bf")
        .that(scanner.getPropertyValue(9407))
        .isEqualTo(9407);
    assertWithMessage("Character 0x24c0 matches caselessly 0x24c0")
        .that(scanner.getPropertyValue(9408))
        .isEqualTo(9408);
    assertWithMessage("Character 0x24c1 matches caselessly 0x24c1")
        .that(scanner.getPropertyValue(9409))
        .isEqualTo(9409);
    assertWithMessage("Character 0x24c2 matches caselessly 0x24c2")
        .that(scanner.getPropertyValue(9410))
        .isEqualTo(9410);
    assertWithMessage("Character 0x24c3 matches caselessly 0x24c3")
        .that(scanner.getPropertyValue(9411))
        .isEqualTo(9411);
    assertWithMessage("Character 0x24c4 matches caselessly 0x24c4")
        .that(scanner.getPropertyValue(9412))
        .isEqualTo(9412);
    assertWithMessage("Character 0x24c5 matches caselessly 0x24c5")
        .that(scanner.getPropertyValue(9413))
        .isEqualTo(9413);
    assertWithMessage("Character 0x24c6 matches caselessly 0x24c6")
        .that(scanner.getPropertyValue(9414))
        .isEqualTo(9414);
    assertWithMessage("Character 0x24c7 matches caselessly 0x24c7")
        .that(scanner.getPropertyValue(9415))
        .isEqualTo(9415);
    assertWithMessage("Character 0x24c8 matches caselessly 0x24c8")
        .that(scanner.getPropertyValue(9416))
        .isEqualTo(9416);
    assertWithMessage("Character 0x24c9 matches caselessly 0x24c9")
        .that(scanner.getPropertyValue(9417))
        .isEqualTo(9417);
    assertWithMessage("Character 0x24ca matches caselessly 0x24ca")
        .that(scanner.getPropertyValue(9418))
        .isEqualTo(9418);
    assertWithMessage("Character 0x24cb matches caselessly 0x24cb")
        .that(scanner.getPropertyValue(9419))
        .isEqualTo(9419);
    assertWithMessage("Character 0x24cc matches caselessly 0x24cc")
        .that(scanner.getPropertyValue(9420))
        .isEqualTo(9420);
    assertWithMessage("Character 0x24cd matches caselessly 0x24cd")
        .that(scanner.getPropertyValue(9421))
        .isEqualTo(9421);
    assertWithMessage("Character 0x24ce matches caselessly 0x24ce")
        .that(scanner.getPropertyValue(9422))
        .isEqualTo(9422);
    assertWithMessage("Character 0x24cf matches caselessly 0x24cf")
        .that(scanner.getPropertyValue(9423))
        .isEqualTo(9423);
    assertWithMessage("Character 0x24d0 matches caselessly 0x24b6")
        .that(scanner.getPropertyValue(9424))
        .isEqualTo(9398);
    assertWithMessage("Character 0x24d1 matches caselessly 0x24b7")
        .that(scanner.getPropertyValue(9425))
        .isEqualTo(9399);
    assertWithMessage("Character 0x24d2 matches caselessly 0x24b8")
        .that(scanner.getPropertyValue(9426))
        .isEqualTo(9400);
    assertWithMessage("Character 0x24d3 matches caselessly 0x24b9")
        .that(scanner.getPropertyValue(9427))
        .isEqualTo(9401);
    assertWithMessage("Character 0x24d4 matches caselessly 0x24ba")
        .that(scanner.getPropertyValue(9428))
        .isEqualTo(9402);
    assertWithMessage("Character 0x24d5 matches caselessly 0x24bb")
        .that(scanner.getPropertyValue(9429))
        .isEqualTo(9403);
    assertWithMessage("Character 0x24d6 matches caselessly 0x24bc")
        .that(scanner.getPropertyValue(9430))
        .isEqualTo(9404);
    assertWithMessage("Character 0x24d7 matches caselessly 0x24bd")
        .that(scanner.getPropertyValue(9431))
        .isEqualTo(9405);
    assertWithMessage("Character 0x24d8 matches caselessly 0x24be")
        .that(scanner.getPropertyValue(9432))
        .isEqualTo(9406);
    assertWithMessage("Character 0x24d9 matches caselessly 0x24bf")
        .that(scanner.getPropertyValue(9433))
        .isEqualTo(9407);
    assertWithMessage("Character 0x24da matches caselessly 0x24c0")
        .that(scanner.getPropertyValue(9434))
        .isEqualTo(9408);
    assertWithMessage("Character 0x24db matches caselessly 0x24c1")
        .that(scanner.getPropertyValue(9435))
        .isEqualTo(9409);
    assertWithMessage("Character 0x24dc matches caselessly 0x24c2")
        .that(scanner.getPropertyValue(9436))
        .isEqualTo(9410);
    assertWithMessage("Character 0x24dd matches caselessly 0x24c3")
        .that(scanner.getPropertyValue(9437))
        .isEqualTo(9411);
    assertWithMessage("Character 0x24de matches caselessly 0x24c4")
        .that(scanner.getPropertyValue(9438))
        .isEqualTo(9412);
    assertWithMessage("Character 0x24df matches caselessly 0x24c5")
        .that(scanner.getPropertyValue(9439))
        .isEqualTo(9413);
    assertWithMessage("Character 0x24e0 matches caselessly 0x24c6")
        .that(scanner.getPropertyValue(9440))
        .isEqualTo(9414);
    assertWithMessage("Character 0x24e1 matches caselessly 0x24c7")
        .that(scanner.getPropertyValue(9441))
        .isEqualTo(9415);
    assertWithMessage("Character 0x24e2 matches caselessly 0x24c8")
        .that(scanner.getPropertyValue(9442))
        .isEqualTo(9416);
    assertWithMessage("Character 0x24e3 matches caselessly 0x24c9")
        .that(scanner.getPropertyValue(9443))
        .isEqualTo(9417);
    assertWithMessage("Character 0x24e4 matches caselessly 0x24ca")
        .that(scanner.getPropertyValue(9444))
        .isEqualTo(9418);
    assertWithMessage("Character 0x24e5 matches caselessly 0x24cb")
        .that(scanner.getPropertyValue(9445))
        .isEqualTo(9419);
    assertWithMessage("Character 0x24e6 matches caselessly 0x24cc")
        .that(scanner.getPropertyValue(9446))
        .isEqualTo(9420);
    assertWithMessage("Character 0x24e7 matches caselessly 0x24cd")
        .that(scanner.getPropertyValue(9447))
        .isEqualTo(9421);
    assertWithMessage("Character 0x24e8 matches caselessly 0x24ce")
        .that(scanner.getPropertyValue(9448))
        .isEqualTo(9422);
    assertWithMessage("Character 0x24e9 matches caselessly 0x24cf")
        .that(scanner.getPropertyValue(9449))
        .isEqualTo(9423);
    assertWithMessage("Character 0x2c00 matches caselessly 0x2c00")
        .that(scanner.getPropertyValue(11264))
        .isEqualTo(11264);
    assertWithMessage("Character 0x2c01 matches caselessly 0x2c01")
        .that(scanner.getPropertyValue(11265))
        .isEqualTo(11265);
    assertWithMessage("Character 0x2c02 matches caselessly 0x2c02")
        .that(scanner.getPropertyValue(11266))
        .isEqualTo(11266);
    assertWithMessage("Character 0x2c03 matches caselessly 0x2c03")
        .that(scanner.getPropertyValue(11267))
        .isEqualTo(11267);
    assertWithMessage("Character 0x2c04 matches caselessly 0x2c04")
        .that(scanner.getPropertyValue(11268))
        .isEqualTo(11268);
    assertWithMessage("Character 0x2c05 matches caselessly 0x2c05")
        .that(scanner.getPropertyValue(11269))
        .isEqualTo(11269);
    assertWithMessage("Character 0x2c06 matches caselessly 0x2c06")
        .that(scanner.getPropertyValue(11270))
        .isEqualTo(11270);
    assertWithMessage("Character 0x2c07 matches caselessly 0x2c07")
        .that(scanner.getPropertyValue(11271))
        .isEqualTo(11271);
    assertWithMessage("Character 0x2c08 matches caselessly 0x2c08")
        .that(scanner.getPropertyValue(11272))
        .isEqualTo(11272);
    assertWithMessage("Character 0x2c09 matches caselessly 0x2c09")
        .that(scanner.getPropertyValue(11273))
        .isEqualTo(11273);
    assertWithMessage("Character 0x2c0a matches caselessly 0x2c0a")
        .that(scanner.getPropertyValue(11274))
        .isEqualTo(11274);
    assertWithMessage("Character 0x2c0b matches caselessly 0x2c0b")
        .that(scanner.getPropertyValue(11275))
        .isEqualTo(11275);
    assertWithMessage("Character 0x2c0c matches caselessly 0x2c0c")
        .that(scanner.getPropertyValue(11276))
        .isEqualTo(11276);
    assertWithMessage("Character 0x2c0d matches caselessly 0x2c0d")
        .that(scanner.getPropertyValue(11277))
        .isEqualTo(11277);
    assertWithMessage("Character 0x2c0e matches caselessly 0x2c0e")
        .that(scanner.getPropertyValue(11278))
        .isEqualTo(11278);
    assertWithMessage("Character 0x2c0f matches caselessly 0x2c0f")
        .that(scanner.getPropertyValue(11279))
        .isEqualTo(11279);
    assertWithMessage("Character 0x2c10 matches caselessly 0x2c10")
        .that(scanner.getPropertyValue(11280))
        .isEqualTo(11280);
    assertWithMessage("Character 0x2c11 matches caselessly 0x2c11")
        .that(scanner.getPropertyValue(11281))
        .isEqualTo(11281);
    assertWithMessage("Character 0x2c12 matches caselessly 0x2c12")
        .that(scanner.getPropertyValue(11282))
        .isEqualTo(11282);
    assertWithMessage("Character 0x2c13 matches caselessly 0x2c13")
        .that(scanner.getPropertyValue(11283))
        .isEqualTo(11283);
    assertWithMessage("Character 0x2c14 matches caselessly 0x2c14")
        .that(scanner.getPropertyValue(11284))
        .isEqualTo(11284);
    assertWithMessage("Character 0x2c15 matches caselessly 0x2c15")
        .that(scanner.getPropertyValue(11285))
        .isEqualTo(11285);
    assertWithMessage("Character 0x2c16 matches caselessly 0x2c16")
        .that(scanner.getPropertyValue(11286))
        .isEqualTo(11286);
    assertWithMessage("Character 0x2c17 matches caselessly 0x2c17")
        .that(scanner.getPropertyValue(11287))
        .isEqualTo(11287);
    assertWithMessage("Character 0x2c18 matches caselessly 0x2c18")
        .that(scanner.getPropertyValue(11288))
        .isEqualTo(11288);
    assertWithMessage("Character 0x2c19 matches caselessly 0x2c19")
        .that(scanner.getPropertyValue(11289))
        .isEqualTo(11289);
    assertWithMessage("Character 0x2c1a matches caselessly 0x2c1a")
        .that(scanner.getPropertyValue(11290))
        .isEqualTo(11290);
    assertWithMessage("Character 0x2c1b matches caselessly 0x2c1b")
        .that(scanner.getPropertyValue(11291))
        .isEqualTo(11291);
    assertWithMessage("Character 0x2c1c matches caselessly 0x2c1c")
        .that(scanner.getPropertyValue(11292))
        .isEqualTo(11292);
    assertWithMessage("Character 0x2c1d matches caselessly 0x2c1d")
        .that(scanner.getPropertyValue(11293))
        .isEqualTo(11293);
    assertWithMessage("Character 0x2c1e matches caselessly 0x2c1e")
        .that(scanner.getPropertyValue(11294))
        .isEqualTo(11294);
    assertWithMessage("Character 0x2c1f matches caselessly 0x2c1f")
        .that(scanner.getPropertyValue(11295))
        .isEqualTo(11295);
    assertWithMessage("Character 0x2c20 matches caselessly 0x2c20")
        .that(scanner.getPropertyValue(11296))
        .isEqualTo(11296);
    assertWithMessage("Character 0x2c21 matches caselessly 0x2c21")
        .that(scanner.getPropertyValue(11297))
        .isEqualTo(11297);
    assertWithMessage("Character 0x2c22 matches caselessly 0x2c22")
        .that(scanner.getPropertyValue(11298))
        .isEqualTo(11298);
    assertWithMessage("Character 0x2c23 matches caselessly 0x2c23")
        .that(scanner.getPropertyValue(11299))
        .isEqualTo(11299);
    assertWithMessage("Character 0x2c24 matches caselessly 0x2c24")
        .that(scanner.getPropertyValue(11300))
        .isEqualTo(11300);
    assertWithMessage("Character 0x2c25 matches caselessly 0x2c25")
        .that(scanner.getPropertyValue(11301))
        .isEqualTo(11301);
    assertWithMessage("Character 0x2c26 matches caselessly 0x2c26")
        .that(scanner.getPropertyValue(11302))
        .isEqualTo(11302);
    assertWithMessage("Character 0x2c27 matches caselessly 0x2c27")
        .that(scanner.getPropertyValue(11303))
        .isEqualTo(11303);
    assertWithMessage("Character 0x2c28 matches caselessly 0x2c28")
        .that(scanner.getPropertyValue(11304))
        .isEqualTo(11304);
    assertWithMessage("Character 0x2c29 matches caselessly 0x2c29")
        .that(scanner.getPropertyValue(11305))
        .isEqualTo(11305);
    assertWithMessage("Character 0x2c2a matches caselessly 0x2c2a")
        .that(scanner.getPropertyValue(11306))
        .isEqualTo(11306);
    assertWithMessage("Character 0x2c2b matches caselessly 0x2c2b")
        .that(scanner.getPropertyValue(11307))
        .isEqualTo(11307);
    assertWithMessage("Character 0x2c2c matches caselessly 0x2c2c")
        .that(scanner.getPropertyValue(11308))
        .isEqualTo(11308);
    assertWithMessage("Character 0x2c2d matches caselessly 0x2c2d")
        .that(scanner.getPropertyValue(11309))
        .isEqualTo(11309);
    assertWithMessage("Character 0x2c2e matches caselessly 0x2c2e")
        .that(scanner.getPropertyValue(11310))
        .isEqualTo(11310);
    assertWithMessage("Character 0x2c30 matches caselessly 0x2c00")
        .that(scanner.getPropertyValue(11312))
        .isEqualTo(11264);
    assertWithMessage("Character 0x2c31 matches caselessly 0x2c01")
        .that(scanner.getPropertyValue(11313))
        .isEqualTo(11265);
    assertWithMessage("Character 0x2c32 matches caselessly 0x2c02")
        .that(scanner.getPropertyValue(11314))
        .isEqualTo(11266);
    assertWithMessage("Character 0x2c33 matches caselessly 0x2c03")
        .that(scanner.getPropertyValue(11315))
        .isEqualTo(11267);
    assertWithMessage("Character 0x2c34 matches caselessly 0x2c04")
        .that(scanner.getPropertyValue(11316))
        .isEqualTo(11268);
    assertWithMessage("Character 0x2c35 matches caselessly 0x2c05")
        .that(scanner.getPropertyValue(11317))
        .isEqualTo(11269);
    assertWithMessage("Character 0x2c36 matches caselessly 0x2c06")
        .that(scanner.getPropertyValue(11318))
        .isEqualTo(11270);
    assertWithMessage("Character 0x2c37 matches caselessly 0x2c07")
        .that(scanner.getPropertyValue(11319))
        .isEqualTo(11271);
    assertWithMessage("Character 0x2c38 matches caselessly 0x2c08")
        .that(scanner.getPropertyValue(11320))
        .isEqualTo(11272);
    assertWithMessage("Character 0x2c39 matches caselessly 0x2c09")
        .that(scanner.getPropertyValue(11321))
        .isEqualTo(11273);
    assertWithMessage("Character 0x2c3a matches caselessly 0x2c0a")
        .that(scanner.getPropertyValue(11322))
        .isEqualTo(11274);
    assertWithMessage("Character 0x2c3b matches caselessly 0x2c0b")
        .that(scanner.getPropertyValue(11323))
        .isEqualTo(11275);
    assertWithMessage("Character 0x2c3c matches caselessly 0x2c0c")
        .that(scanner.getPropertyValue(11324))
        .isEqualTo(11276);
    assertWithMessage("Character 0x2c3d matches caselessly 0x2c0d")
        .that(scanner.getPropertyValue(11325))
        .isEqualTo(11277);
    assertWithMessage("Character 0x2c3e matches caselessly 0x2c0e")
        .that(scanner.getPropertyValue(11326))
        .isEqualTo(11278);
    assertWithMessage("Character 0x2c3f matches caselessly 0x2c0f")
        .that(scanner.getPropertyValue(11327))
        .isEqualTo(11279);
    assertWithMessage("Character 0x2c40 matches caselessly 0x2c10")
        .that(scanner.getPropertyValue(11328))
        .isEqualTo(11280);
    assertWithMessage("Character 0x2c41 matches caselessly 0x2c11")
        .that(scanner.getPropertyValue(11329))
        .isEqualTo(11281);
    assertWithMessage("Character 0x2c42 matches caselessly 0x2c12")
        .that(scanner.getPropertyValue(11330))
        .isEqualTo(11282);
    assertWithMessage("Character 0x2c43 matches caselessly 0x2c13")
        .that(scanner.getPropertyValue(11331))
        .isEqualTo(11283);
    assertWithMessage("Character 0x2c44 matches caselessly 0x2c14")
        .that(scanner.getPropertyValue(11332))
        .isEqualTo(11284);
    assertWithMessage("Character 0x2c45 matches caselessly 0x2c15")
        .that(scanner.getPropertyValue(11333))
        .isEqualTo(11285);
    assertWithMessage("Character 0x2c46 matches caselessly 0x2c16")
        .that(scanner.getPropertyValue(11334))
        .isEqualTo(11286);
    assertWithMessage("Character 0x2c47 matches caselessly 0x2c17")
        .that(scanner.getPropertyValue(11335))
        .isEqualTo(11287);
    assertWithMessage("Character 0x2c48 matches caselessly 0x2c18")
        .that(scanner.getPropertyValue(11336))
        .isEqualTo(11288);
    assertWithMessage("Character 0x2c49 matches caselessly 0x2c19")
        .that(scanner.getPropertyValue(11337))
        .isEqualTo(11289);
    assertWithMessage("Character 0x2c4a matches caselessly 0x2c1a")
        .that(scanner.getPropertyValue(11338))
        .isEqualTo(11290);
    assertWithMessage("Character 0x2c4b matches caselessly 0x2c1b")
        .that(scanner.getPropertyValue(11339))
        .isEqualTo(11291);
    assertWithMessage("Character 0x2c4c matches caselessly 0x2c1c")
        .that(scanner.getPropertyValue(11340))
        .isEqualTo(11292);
    assertWithMessage("Character 0x2c4d matches caselessly 0x2c1d")
        .that(scanner.getPropertyValue(11341))
        .isEqualTo(11293);
    assertWithMessage("Character 0x2c4e matches caselessly 0x2c1e")
        .that(scanner.getPropertyValue(11342))
        .isEqualTo(11294);
    assertWithMessage("Character 0x2c4f matches caselessly 0x2c1f")
        .that(scanner.getPropertyValue(11343))
        .isEqualTo(11295);
    assertWithMessage("Character 0x2c50 matches caselessly 0x2c20")
        .that(scanner.getPropertyValue(11344))
        .isEqualTo(11296);
    assertWithMessage("Character 0x2c51 matches caselessly 0x2c21")
        .that(scanner.getPropertyValue(11345))
        .isEqualTo(11297);
    assertWithMessage("Character 0x2c52 matches caselessly 0x2c22")
        .that(scanner.getPropertyValue(11346))
        .isEqualTo(11298);
    assertWithMessage("Character 0x2c53 matches caselessly 0x2c23")
        .that(scanner.getPropertyValue(11347))
        .isEqualTo(11299);
    assertWithMessage("Character 0x2c54 matches caselessly 0x2c24")
        .that(scanner.getPropertyValue(11348))
        .isEqualTo(11300);
    assertWithMessage("Character 0x2c55 matches caselessly 0x2c25")
        .that(scanner.getPropertyValue(11349))
        .isEqualTo(11301);
    assertWithMessage("Character 0x2c56 matches caselessly 0x2c26")
        .that(scanner.getPropertyValue(11350))
        .isEqualTo(11302);
    assertWithMessage("Character 0x2c57 matches caselessly 0x2c27")
        .that(scanner.getPropertyValue(11351))
        .isEqualTo(11303);
    assertWithMessage("Character 0x2c58 matches caselessly 0x2c28")
        .that(scanner.getPropertyValue(11352))
        .isEqualTo(11304);
    assertWithMessage("Character 0x2c59 matches caselessly 0x2c29")
        .that(scanner.getPropertyValue(11353))
        .isEqualTo(11305);
    assertWithMessage("Character 0x2c5a matches caselessly 0x2c2a")
        .that(scanner.getPropertyValue(11354))
        .isEqualTo(11306);
    assertWithMessage("Character 0x2c5b matches caselessly 0x2c2b")
        .that(scanner.getPropertyValue(11355))
        .isEqualTo(11307);
    assertWithMessage("Character 0x2c5c matches caselessly 0x2c2c")
        .that(scanner.getPropertyValue(11356))
        .isEqualTo(11308);
    assertWithMessage("Character 0x2c5d matches caselessly 0x2c2d")
        .that(scanner.getPropertyValue(11357))
        .isEqualTo(11309);
    assertWithMessage("Character 0x2c5e matches caselessly 0x2c2e")
        .that(scanner.getPropertyValue(11358))
        .isEqualTo(11310);
    assertWithMessage("Character 0x2c80 matches caselessly 0x2c80")
        .that(scanner.getPropertyValue(11392))
        .isEqualTo(11392);
    assertWithMessage("Character 0x2c81 matches caselessly 0x2c80")
        .that(scanner.getPropertyValue(11393))
        .isEqualTo(11392);
    assertWithMessage("Character 0x2c82 matches caselessly 0x2c82")
        .that(scanner.getPropertyValue(11394))
        .isEqualTo(11394);
    assertWithMessage("Character 0x2c83 matches caselessly 0x2c82")
        .that(scanner.getPropertyValue(11395))
        .isEqualTo(11394);
    assertWithMessage("Character 0x2c84 matches caselessly 0x2c84")
        .that(scanner.getPropertyValue(11396))
        .isEqualTo(11396);
    assertWithMessage("Character 0x2c85 matches caselessly 0x2c84")
        .that(scanner.getPropertyValue(11397))
        .isEqualTo(11396);
    assertWithMessage("Character 0x2c86 matches caselessly 0x2c86")
        .that(scanner.getPropertyValue(11398))
        .isEqualTo(11398);
    assertWithMessage("Character 0x2c87 matches caselessly 0x2c86")
        .that(scanner.getPropertyValue(11399))
        .isEqualTo(11398);
    assertWithMessage("Character 0x2c88 matches caselessly 0x2c88")
        .that(scanner.getPropertyValue(11400))
        .isEqualTo(11400);
    assertWithMessage("Character 0x2c89 matches caselessly 0x2c88")
        .that(scanner.getPropertyValue(11401))
        .isEqualTo(11400);
    assertWithMessage("Character 0x2c8a matches caselessly 0x2c8a")
        .that(scanner.getPropertyValue(11402))
        .isEqualTo(11402);
    assertWithMessage("Character 0x2c8b matches caselessly 0x2c8a")
        .that(scanner.getPropertyValue(11403))
        .isEqualTo(11402);
    assertWithMessage("Character 0x2c8c matches caselessly 0x2c8c")
        .that(scanner.getPropertyValue(11404))
        .isEqualTo(11404);
    assertWithMessage("Character 0x2c8d matches caselessly 0x2c8c")
        .that(scanner.getPropertyValue(11405))
        .isEqualTo(11404);
    assertWithMessage("Character 0x2c8e matches caselessly 0x2c8e")
        .that(scanner.getPropertyValue(11406))
        .isEqualTo(11406);
    assertWithMessage("Character 0x2c8f matches caselessly 0x2c8e")
        .that(scanner.getPropertyValue(11407))
        .isEqualTo(11406);
    assertWithMessage("Character 0x2c90 matches caselessly 0x2c90")
        .that(scanner.getPropertyValue(11408))
        .isEqualTo(11408);
    assertWithMessage("Character 0x2c91 matches caselessly 0x2c90")
        .that(scanner.getPropertyValue(11409))
        .isEqualTo(11408);
    assertWithMessage("Character 0x2c92 matches caselessly 0x2c92")
        .that(scanner.getPropertyValue(11410))
        .isEqualTo(11410);
    assertWithMessage("Character 0x2c93 matches caselessly 0x2c92")
        .that(scanner.getPropertyValue(11411))
        .isEqualTo(11410);
    assertWithMessage("Character 0x2c94 matches caselessly 0x2c94")
        .that(scanner.getPropertyValue(11412))
        .isEqualTo(11412);
    assertWithMessage("Character 0x2c95 matches caselessly 0x2c94")
        .that(scanner.getPropertyValue(11413))
        .isEqualTo(11412);
    assertWithMessage("Character 0x2c96 matches caselessly 0x2c96")
        .that(scanner.getPropertyValue(11414))
        .isEqualTo(11414);
    assertWithMessage("Character 0x2c97 matches caselessly 0x2c96")
        .that(scanner.getPropertyValue(11415))
        .isEqualTo(11414);
    assertWithMessage("Character 0x2c98 matches caselessly 0x2c98")
        .that(scanner.getPropertyValue(11416))
        .isEqualTo(11416);
    assertWithMessage("Character 0x2c99 matches caselessly 0x2c98")
        .that(scanner.getPropertyValue(11417))
        .isEqualTo(11416);
    assertWithMessage("Character 0x2c9a matches caselessly 0x2c9a")
        .that(scanner.getPropertyValue(11418))
        .isEqualTo(11418);
    assertWithMessage("Character 0x2c9b matches caselessly 0x2c9a")
        .that(scanner.getPropertyValue(11419))
        .isEqualTo(11418);
    assertWithMessage("Character 0x2c9c matches caselessly 0x2c9c")
        .that(scanner.getPropertyValue(11420))
        .isEqualTo(11420);
    assertWithMessage("Character 0x2c9d matches caselessly 0x2c9c")
        .that(scanner.getPropertyValue(11421))
        .isEqualTo(11420);
    assertWithMessage("Character 0x2c9e matches caselessly 0x2c9e")
        .that(scanner.getPropertyValue(11422))
        .isEqualTo(11422);
    assertWithMessage("Character 0x2c9f matches caselessly 0x2c9e")
        .that(scanner.getPropertyValue(11423))
        .isEqualTo(11422);
    assertWithMessage("Character 0x2ca0 matches caselessly 0x2ca0")
        .that(scanner.getPropertyValue(11424))
        .isEqualTo(11424);
    assertWithMessage("Character 0x2ca1 matches caselessly 0x2ca0")
        .that(scanner.getPropertyValue(11425))
        .isEqualTo(11424);
    assertWithMessage("Character 0x2ca2 matches caselessly 0x2ca2")
        .that(scanner.getPropertyValue(11426))
        .isEqualTo(11426);
    assertWithMessage("Character 0x2ca3 matches caselessly 0x2ca2")
        .that(scanner.getPropertyValue(11427))
        .isEqualTo(11426);
    assertWithMessage("Character 0x2ca4 matches caselessly 0x2ca4")
        .that(scanner.getPropertyValue(11428))
        .isEqualTo(11428);
    assertWithMessage("Character 0x2ca5 matches caselessly 0x2ca4")
        .that(scanner.getPropertyValue(11429))
        .isEqualTo(11428);
    assertWithMessage("Character 0x2ca6 matches caselessly 0x2ca6")
        .that(scanner.getPropertyValue(11430))
        .isEqualTo(11430);
    assertWithMessage("Character 0x2ca7 matches caselessly 0x2ca6")
        .that(scanner.getPropertyValue(11431))
        .isEqualTo(11430);
    assertWithMessage("Character 0x2ca8 matches caselessly 0x2ca8")
        .that(scanner.getPropertyValue(11432))
        .isEqualTo(11432);
    assertWithMessage("Character 0x2ca9 matches caselessly 0x2ca8")
        .that(scanner.getPropertyValue(11433))
        .isEqualTo(11432);
    assertWithMessage("Character 0x2caa matches caselessly 0x2caa")
        .that(scanner.getPropertyValue(11434))
        .isEqualTo(11434);
    assertWithMessage("Character 0x2cab matches caselessly 0x2caa")
        .that(scanner.getPropertyValue(11435))
        .isEqualTo(11434);
    assertWithMessage("Character 0x2cac matches caselessly 0x2cac")
        .that(scanner.getPropertyValue(11436))
        .isEqualTo(11436);
    assertWithMessage("Character 0x2cad matches caselessly 0x2cac")
        .that(scanner.getPropertyValue(11437))
        .isEqualTo(11436);
    assertWithMessage("Character 0x2cae matches caselessly 0x2cae")
        .that(scanner.getPropertyValue(11438))
        .isEqualTo(11438);
    assertWithMessage("Character 0x2caf matches caselessly 0x2cae")
        .that(scanner.getPropertyValue(11439))
        .isEqualTo(11438);
    assertWithMessage("Character 0x2cb0 matches caselessly 0x2cb0")
        .that(scanner.getPropertyValue(11440))
        .isEqualTo(11440);
    assertWithMessage("Character 0x2cb1 matches caselessly 0x2cb0")
        .that(scanner.getPropertyValue(11441))
        .isEqualTo(11440);
    assertWithMessage("Character 0x2cb2 matches caselessly 0x2cb2")
        .that(scanner.getPropertyValue(11442))
        .isEqualTo(11442);
    assertWithMessage("Character 0x2cb3 matches caselessly 0x2cb2")
        .that(scanner.getPropertyValue(11443))
        .isEqualTo(11442);
    assertWithMessage("Character 0x2cb4 matches caselessly 0x2cb4")
        .that(scanner.getPropertyValue(11444))
        .isEqualTo(11444);
    assertWithMessage("Character 0x2cb5 matches caselessly 0x2cb4")
        .that(scanner.getPropertyValue(11445))
        .isEqualTo(11444);
    assertWithMessage("Character 0x2cb6 matches caselessly 0x2cb6")
        .that(scanner.getPropertyValue(11446))
        .isEqualTo(11446);
    assertWithMessage("Character 0x2cb7 matches caselessly 0x2cb6")
        .that(scanner.getPropertyValue(11447))
        .isEqualTo(11446);
    assertWithMessage("Character 0x2cb8 matches caselessly 0x2cb8")
        .that(scanner.getPropertyValue(11448))
        .isEqualTo(11448);
    assertWithMessage("Character 0x2cb9 matches caselessly 0x2cb8")
        .that(scanner.getPropertyValue(11449))
        .isEqualTo(11448);
    assertWithMessage("Character 0x2cba matches caselessly 0x2cba")
        .that(scanner.getPropertyValue(11450))
        .isEqualTo(11450);
    assertWithMessage("Character 0x2cbb matches caselessly 0x2cba")
        .that(scanner.getPropertyValue(11451))
        .isEqualTo(11450);
    assertWithMessage("Character 0x2cbc matches caselessly 0x2cbc")
        .that(scanner.getPropertyValue(11452))
        .isEqualTo(11452);
    assertWithMessage("Character 0x2cbd matches caselessly 0x2cbc")
        .that(scanner.getPropertyValue(11453))
        .isEqualTo(11452);
    assertWithMessage("Character 0x2cbe matches caselessly 0x2cbe")
        .that(scanner.getPropertyValue(11454))
        .isEqualTo(11454);
    assertWithMessage("Character 0x2cbf matches caselessly 0x2cbe")
        .that(scanner.getPropertyValue(11455))
        .isEqualTo(11454);
    assertWithMessage("Character 0x2cc0 matches caselessly 0x2cc0")
        .that(scanner.getPropertyValue(11456))
        .isEqualTo(11456);
    assertWithMessage("Character 0x2cc1 matches caselessly 0x2cc0")
        .that(scanner.getPropertyValue(11457))
        .isEqualTo(11456);
    assertWithMessage("Character 0x2cc2 matches caselessly 0x2cc2")
        .that(scanner.getPropertyValue(11458))
        .isEqualTo(11458);
    assertWithMessage("Character 0x2cc3 matches caselessly 0x2cc2")
        .that(scanner.getPropertyValue(11459))
        .isEqualTo(11458);
    assertWithMessage("Character 0x2cc4 matches caselessly 0x2cc4")
        .that(scanner.getPropertyValue(11460))
        .isEqualTo(11460);
    assertWithMessage("Character 0x2cc5 matches caselessly 0x2cc4")
        .that(scanner.getPropertyValue(11461))
        .isEqualTo(11460);
    assertWithMessage("Character 0x2cc6 matches caselessly 0x2cc6")
        .that(scanner.getPropertyValue(11462))
        .isEqualTo(11462);
    assertWithMessage("Character 0x2cc7 matches caselessly 0x2cc6")
        .that(scanner.getPropertyValue(11463))
        .isEqualTo(11462);
    assertWithMessage("Character 0x2cc8 matches caselessly 0x2cc8")
        .that(scanner.getPropertyValue(11464))
        .isEqualTo(11464);
    assertWithMessage("Character 0x2cc9 matches caselessly 0x2cc8")
        .that(scanner.getPropertyValue(11465))
        .isEqualTo(11464);
    assertWithMessage("Character 0x2cca matches caselessly 0x2cca")
        .that(scanner.getPropertyValue(11466))
        .isEqualTo(11466);
    assertWithMessage("Character 0x2ccb matches caselessly 0x2cca")
        .that(scanner.getPropertyValue(11467))
        .isEqualTo(11466);
    assertWithMessage("Character 0x2ccc matches caselessly 0x2ccc")
        .that(scanner.getPropertyValue(11468))
        .isEqualTo(11468);
    assertWithMessage("Character 0x2ccd matches caselessly 0x2ccc")
        .that(scanner.getPropertyValue(11469))
        .isEqualTo(11468);
    assertWithMessage("Character 0x2cce matches caselessly 0x2cce")
        .that(scanner.getPropertyValue(11470))
        .isEqualTo(11470);
    assertWithMessage("Character 0x2ccf matches caselessly 0x2cce")
        .that(scanner.getPropertyValue(11471))
        .isEqualTo(11470);
    assertWithMessage("Character 0x2cd0 matches caselessly 0x2cd0")
        .that(scanner.getPropertyValue(11472))
        .isEqualTo(11472);
    assertWithMessage("Character 0x2cd1 matches caselessly 0x2cd0")
        .that(scanner.getPropertyValue(11473))
        .isEqualTo(11472);
    assertWithMessage("Character 0x2cd2 matches caselessly 0x2cd2")
        .that(scanner.getPropertyValue(11474))
        .isEqualTo(11474);
    assertWithMessage("Character 0x2cd3 matches caselessly 0x2cd2")
        .that(scanner.getPropertyValue(11475))
        .isEqualTo(11474);
    assertWithMessage("Character 0x2cd4 matches caselessly 0x2cd4")
        .that(scanner.getPropertyValue(11476))
        .isEqualTo(11476);
    assertWithMessage("Character 0x2cd5 matches caselessly 0x2cd4")
        .that(scanner.getPropertyValue(11477))
        .isEqualTo(11476);
    assertWithMessage("Character 0x2cd6 matches caselessly 0x2cd6")
        .that(scanner.getPropertyValue(11478))
        .isEqualTo(11478);
    assertWithMessage("Character 0x2cd7 matches caselessly 0x2cd6")
        .that(scanner.getPropertyValue(11479))
        .isEqualTo(11478);
    assertWithMessage("Character 0x2cd8 matches caselessly 0x2cd8")
        .that(scanner.getPropertyValue(11480))
        .isEqualTo(11480);
    assertWithMessage("Character 0x2cd9 matches caselessly 0x2cd8")
        .that(scanner.getPropertyValue(11481))
        .isEqualTo(11480);
    assertWithMessage("Character 0x2cda matches caselessly 0x2cda")
        .that(scanner.getPropertyValue(11482))
        .isEqualTo(11482);
    assertWithMessage("Character 0x2cdb matches caselessly 0x2cda")
        .that(scanner.getPropertyValue(11483))
        .isEqualTo(11482);
    assertWithMessage("Character 0x2cdc matches caselessly 0x2cdc")
        .that(scanner.getPropertyValue(11484))
        .isEqualTo(11484);
    assertWithMessage("Character 0x2cdd matches caselessly 0x2cdc")
        .that(scanner.getPropertyValue(11485))
        .isEqualTo(11484);
    assertWithMessage("Character 0x2cde matches caselessly 0x2cde")
        .that(scanner.getPropertyValue(11486))
        .isEqualTo(11486);
    assertWithMessage("Character 0x2cdf matches caselessly 0x2cde")
        .that(scanner.getPropertyValue(11487))
        .isEqualTo(11486);
    assertWithMessage("Character 0x2ce0 matches caselessly 0x2ce0")
        .that(scanner.getPropertyValue(11488))
        .isEqualTo(11488);
    assertWithMessage("Character 0x2ce1 matches caselessly 0x2ce0")
        .that(scanner.getPropertyValue(11489))
        .isEqualTo(11488);
    assertWithMessage("Character 0x2ce2 matches caselessly 0x2ce2")
        .that(scanner.getPropertyValue(11490))
        .isEqualTo(11490);
    assertWithMessage("Character 0x2ce3 matches caselessly 0x2ce2")
        .that(scanner.getPropertyValue(11491))
        .isEqualTo(11490);
    assertWithMessage("Character 0x2d00 matches caselessly 0x10a0")
        .that(scanner.getPropertyValue(11520))
        .isEqualTo(4256);
    assertWithMessage("Character 0x2d01 matches caselessly 0x10a1")
        .that(scanner.getPropertyValue(11521))
        .isEqualTo(4257);
    assertWithMessage("Character 0x2d02 matches caselessly 0x10a2")
        .that(scanner.getPropertyValue(11522))
        .isEqualTo(4258);
    assertWithMessage("Character 0x2d03 matches caselessly 0x10a3")
        .that(scanner.getPropertyValue(11523))
        .isEqualTo(4259);
    assertWithMessage("Character 0x2d04 matches caselessly 0x10a4")
        .that(scanner.getPropertyValue(11524))
        .isEqualTo(4260);
    assertWithMessage("Character 0x2d05 matches caselessly 0x10a5")
        .that(scanner.getPropertyValue(11525))
        .isEqualTo(4261);
    assertWithMessage("Character 0x2d06 matches caselessly 0x10a6")
        .that(scanner.getPropertyValue(11526))
        .isEqualTo(4262);
    assertWithMessage("Character 0x2d07 matches caselessly 0x10a7")
        .that(scanner.getPropertyValue(11527))
        .isEqualTo(4263);
    assertWithMessage("Character 0x2d08 matches caselessly 0x10a8")
        .that(scanner.getPropertyValue(11528))
        .isEqualTo(4264);
    assertWithMessage("Character 0x2d09 matches caselessly 0x10a9")
        .that(scanner.getPropertyValue(11529))
        .isEqualTo(4265);
    assertWithMessage("Character 0x2d0a matches caselessly 0x10aa")
        .that(scanner.getPropertyValue(11530))
        .isEqualTo(4266);
    assertWithMessage("Character 0x2d0b matches caselessly 0x10ab")
        .that(scanner.getPropertyValue(11531))
        .isEqualTo(4267);
    assertWithMessage("Character 0x2d0c matches caselessly 0x10ac")
        .that(scanner.getPropertyValue(11532))
        .isEqualTo(4268);
    assertWithMessage("Character 0x2d0d matches caselessly 0x10ad")
        .that(scanner.getPropertyValue(11533))
        .isEqualTo(4269);
    assertWithMessage("Character 0x2d0e matches caselessly 0x10ae")
        .that(scanner.getPropertyValue(11534))
        .isEqualTo(4270);
    assertWithMessage("Character 0x2d0f matches caselessly 0x10af")
        .that(scanner.getPropertyValue(11535))
        .isEqualTo(4271);
    assertWithMessage("Character 0x2d10 matches caselessly 0x10b0")
        .that(scanner.getPropertyValue(11536))
        .isEqualTo(4272);
    assertWithMessage("Character 0x2d11 matches caselessly 0x10b1")
        .that(scanner.getPropertyValue(11537))
        .isEqualTo(4273);
    assertWithMessage("Character 0x2d12 matches caselessly 0x10b2")
        .that(scanner.getPropertyValue(11538))
        .isEqualTo(4274);
    assertWithMessage("Character 0x2d13 matches caselessly 0x10b3")
        .that(scanner.getPropertyValue(11539))
        .isEqualTo(4275);
    assertWithMessage("Character 0x2d14 matches caselessly 0x10b4")
        .that(scanner.getPropertyValue(11540))
        .isEqualTo(4276);
    assertWithMessage("Character 0x2d15 matches caselessly 0x10b5")
        .that(scanner.getPropertyValue(11541))
        .isEqualTo(4277);
    assertWithMessage("Character 0x2d16 matches caselessly 0x10b6")
        .that(scanner.getPropertyValue(11542))
        .isEqualTo(4278);
    assertWithMessage("Character 0x2d17 matches caselessly 0x10b7")
        .that(scanner.getPropertyValue(11543))
        .isEqualTo(4279);
    assertWithMessage("Character 0x2d18 matches caselessly 0x10b8")
        .that(scanner.getPropertyValue(11544))
        .isEqualTo(4280);
    assertWithMessage("Character 0x2d19 matches caselessly 0x10b9")
        .that(scanner.getPropertyValue(11545))
        .isEqualTo(4281);
    assertWithMessage("Character 0x2d1a matches caselessly 0x10ba")
        .that(scanner.getPropertyValue(11546))
        .isEqualTo(4282);
    assertWithMessage("Character 0x2d1b matches caselessly 0x10bb")
        .that(scanner.getPropertyValue(11547))
        .isEqualTo(4283);
    assertWithMessage("Character 0x2d1c matches caselessly 0x10bc")
        .that(scanner.getPropertyValue(11548))
        .isEqualTo(4284);
    assertWithMessage("Character 0x2d1d matches caselessly 0x10bd")
        .that(scanner.getPropertyValue(11549))
        .isEqualTo(4285);
    assertWithMessage("Character 0x2d1e matches caselessly 0x10be")
        .that(scanner.getPropertyValue(11550))
        .isEqualTo(4286);
    assertWithMessage("Character 0x2d1f matches caselessly 0x10bf")
        .that(scanner.getPropertyValue(11551))
        .isEqualTo(4287);
    assertWithMessage("Character 0x2d20 matches caselessly 0x10c0")
        .that(scanner.getPropertyValue(11552))
        .isEqualTo(4288);
    assertWithMessage("Character 0x2d21 matches caselessly 0x10c1")
        .that(scanner.getPropertyValue(11553))
        .isEqualTo(4289);
    assertWithMessage("Character 0x2d22 matches caselessly 0x10c2")
        .that(scanner.getPropertyValue(11554))
        .isEqualTo(4290);
    assertWithMessage("Character 0x2d23 matches caselessly 0x10c3")
        .that(scanner.getPropertyValue(11555))
        .isEqualTo(4291);
    assertWithMessage("Character 0x2d24 matches caselessly 0x10c4")
        .that(scanner.getPropertyValue(11556))
        .isEqualTo(4292);
    assertWithMessage("Character 0x2d25 matches caselessly 0x10c5")
        .that(scanner.getPropertyValue(11557))
        .isEqualTo(4293);
    assertWithMessage("Character 0xff21 matches caselessly 0xff21")
        .that(scanner.getPropertyValue(65313))
        .isEqualTo(65313);
    assertWithMessage("Character 0xff22 matches caselessly 0xff22")
        .that(scanner.getPropertyValue(65314))
        .isEqualTo(65314);
    assertWithMessage("Character 0xff23 matches caselessly 0xff23")
        .that(scanner.getPropertyValue(65315))
        .isEqualTo(65315);
    assertWithMessage("Character 0xff24 matches caselessly 0xff24")
        .that(scanner.getPropertyValue(65316))
        .isEqualTo(65316);
    assertWithMessage("Character 0xff25 matches caselessly 0xff25")
        .that(scanner.getPropertyValue(65317))
        .isEqualTo(65317);
    assertWithMessage("Character 0xff26 matches caselessly 0xff26")
        .that(scanner.getPropertyValue(65318))
        .isEqualTo(65318);
    assertWithMessage("Character 0xff27 matches caselessly 0xff27")
        .that(scanner.getPropertyValue(65319))
        .isEqualTo(65319);
    assertWithMessage("Character 0xff28 matches caselessly 0xff28")
        .that(scanner.getPropertyValue(65320))
        .isEqualTo(65320);
    assertWithMessage("Character 0xff29 matches caselessly 0xff29")
        .that(scanner.getPropertyValue(65321))
        .isEqualTo(65321);
    assertWithMessage("Character 0xff2a matches caselessly 0xff2a")
        .that(scanner.getPropertyValue(65322))
        .isEqualTo(65322);
    assertWithMessage("Character 0xff2b matches caselessly 0xff2b")
        .that(scanner.getPropertyValue(65323))
        .isEqualTo(65323);
    assertWithMessage("Character 0xff2c matches caselessly 0xff2c")
        .that(scanner.getPropertyValue(65324))
        .isEqualTo(65324);
    assertWithMessage("Character 0xff2d matches caselessly 0xff2d")
        .that(scanner.getPropertyValue(65325))
        .isEqualTo(65325);
    assertWithMessage("Character 0xff2e matches caselessly 0xff2e")
        .that(scanner.getPropertyValue(65326))
        .isEqualTo(65326);
    assertWithMessage("Character 0xff2f matches caselessly 0xff2f")
        .that(scanner.getPropertyValue(65327))
        .isEqualTo(65327);
    assertWithMessage("Character 0xff30 matches caselessly 0xff30")
        .that(scanner.getPropertyValue(65328))
        .isEqualTo(65328);
    assertWithMessage("Character 0xff31 matches caselessly 0xff31")
        .that(scanner.getPropertyValue(65329))
        .isEqualTo(65329);
    assertWithMessage("Character 0xff32 matches caselessly 0xff32")
        .that(scanner.getPropertyValue(65330))
        .isEqualTo(65330);
    assertWithMessage("Character 0xff33 matches caselessly 0xff33")
        .that(scanner.getPropertyValue(65331))
        .isEqualTo(65331);
    assertWithMessage("Character 0xff34 matches caselessly 0xff34")
        .that(scanner.getPropertyValue(65332))
        .isEqualTo(65332);
    assertWithMessage("Character 0xff35 matches caselessly 0xff35")
        .that(scanner.getPropertyValue(65333))
        .isEqualTo(65333);
    assertWithMessage("Character 0xff36 matches caselessly 0xff36")
        .that(scanner.getPropertyValue(65334))
        .isEqualTo(65334);
    assertWithMessage("Character 0xff37 matches caselessly 0xff37")
        .that(scanner.getPropertyValue(65335))
        .isEqualTo(65335);
    assertWithMessage("Character 0xff38 matches caselessly 0xff38")
        .that(scanner.getPropertyValue(65336))
        .isEqualTo(65336);
    assertWithMessage("Character 0xff39 matches caselessly 0xff39")
        .that(scanner.getPropertyValue(65337))
        .isEqualTo(65337);
    assertWithMessage("Character 0xff3a matches caselessly 0xff3a")
        .that(scanner.getPropertyValue(65338))
        .isEqualTo(65338);
    assertWithMessage("Character 0xff41 matches caselessly 0xff21")
        .that(scanner.getPropertyValue(65345))
        .isEqualTo(65313);
    assertWithMessage("Character 0xff42 matches caselessly 0xff22")
        .that(scanner.getPropertyValue(65346))
        .isEqualTo(65314);
    assertWithMessage("Character 0xff43 matches caselessly 0xff23")
        .that(scanner.getPropertyValue(65347))
        .isEqualTo(65315);
    assertWithMessage("Character 0xff44 matches caselessly 0xff24")
        .that(scanner.getPropertyValue(65348))
        .isEqualTo(65316);
    assertWithMessage("Character 0xff45 matches caselessly 0xff25")
        .that(scanner.getPropertyValue(65349))
        .isEqualTo(65317);
    assertWithMessage("Character 0xff46 matches caselessly 0xff26")
        .that(scanner.getPropertyValue(65350))
        .isEqualTo(65318);
    assertWithMessage("Character 0xff47 matches caselessly 0xff27")
        .that(scanner.getPropertyValue(65351))
        .isEqualTo(65319);
    assertWithMessage("Character 0xff48 matches caselessly 0xff28")
        .that(scanner.getPropertyValue(65352))
        .isEqualTo(65320);
    assertWithMessage("Character 0xff49 matches caselessly 0xff29")
        .that(scanner.getPropertyValue(65353))
        .isEqualTo(65321);
    assertWithMessage("Character 0xff4a matches caselessly 0xff2a")
        .that(scanner.getPropertyValue(65354))
        .isEqualTo(65322);
    assertWithMessage("Character 0xff4b matches caselessly 0xff2b")
        .that(scanner.getPropertyValue(65355))
        .isEqualTo(65323);
    assertWithMessage("Character 0xff4c matches caselessly 0xff2c")
        .that(scanner.getPropertyValue(65356))
        .isEqualTo(65324);
    assertWithMessage("Character 0xff4d matches caselessly 0xff2d")
        .that(scanner.getPropertyValue(65357))
        .isEqualTo(65325);
    assertWithMessage("Character 0xff4e matches caselessly 0xff2e")
        .that(scanner.getPropertyValue(65358))
        .isEqualTo(65326);
    assertWithMessage("Character 0xff4f matches caselessly 0xff2f")
        .that(scanner.getPropertyValue(65359))
        .isEqualTo(65327);
    assertWithMessage("Character 0xff50 matches caselessly 0xff30")
        .that(scanner.getPropertyValue(65360))
        .isEqualTo(65328);
    assertWithMessage("Character 0xff51 matches caselessly 0xff31")
        .that(scanner.getPropertyValue(65361))
        .isEqualTo(65329);
    assertWithMessage("Character 0xff52 matches caselessly 0xff32")
        .that(scanner.getPropertyValue(65362))
        .isEqualTo(65330);
    assertWithMessage("Character 0xff53 matches caselessly 0xff33")
        .that(scanner.getPropertyValue(65363))
        .isEqualTo(65331);
    assertWithMessage("Character 0xff54 matches caselessly 0xff34")
        .that(scanner.getPropertyValue(65364))
        .isEqualTo(65332);
    assertWithMessage("Character 0xff55 matches caselessly 0xff35")
        .that(scanner.getPropertyValue(65365))
        .isEqualTo(65333);
    assertWithMessage("Character 0xff56 matches caselessly 0xff36")
        .that(scanner.getPropertyValue(65366))
        .isEqualTo(65334);
    assertWithMessage("Character 0xff57 matches caselessly 0xff37")
        .that(scanner.getPropertyValue(65367))
        .isEqualTo(65335);
    assertWithMessage("Character 0xff58 matches caselessly 0xff38")
        .that(scanner.getPropertyValue(65368))
        .isEqualTo(65336);
    assertWithMessage("Character 0xff59 matches caselessly 0xff39")
        .that(scanner.getPropertyValue(65369))
        .isEqualTo(65337);
    assertWithMessage("Character 0xff5a matches caselessly 0xff3a")
        .that(scanner.getPropertyValue(65370))
        .isEqualTo(65338);
    assertWithMessage("Character 0x10400 matches caselessly 0x10400")
        .that(scanner.getPropertyValue(66560))
        .isEqualTo(66560);
    assertWithMessage("Character 0x10401 matches caselessly 0x10401")
        .that(scanner.getPropertyValue(66561))
        .isEqualTo(66561);
    assertWithMessage("Character 0x10402 matches caselessly 0x10402")
        .that(scanner.getPropertyValue(66562))
        .isEqualTo(66562);
    assertWithMessage("Character 0x10403 matches caselessly 0x10403")
        .that(scanner.getPropertyValue(66563))
        .isEqualTo(66563);
    assertWithMessage("Character 0x10404 matches caselessly 0x10404")
        .that(scanner.getPropertyValue(66564))
        .isEqualTo(66564);
    assertWithMessage("Character 0x10405 matches caselessly 0x10405")
        .that(scanner.getPropertyValue(66565))
        .isEqualTo(66565);
    assertWithMessage("Character 0x10406 matches caselessly 0x10406")
        .that(scanner.getPropertyValue(66566))
        .isEqualTo(66566);
    assertWithMessage("Character 0x10407 matches caselessly 0x10407")
        .that(scanner.getPropertyValue(66567))
        .isEqualTo(66567);
    assertWithMessage("Character 0x10408 matches caselessly 0x10408")
        .that(scanner.getPropertyValue(66568))
        .isEqualTo(66568);
    assertWithMessage("Character 0x10409 matches caselessly 0x10409")
        .that(scanner.getPropertyValue(66569))
        .isEqualTo(66569);
    assertWithMessage("Character 0x1040a matches caselessly 0x1040a")
        .that(scanner.getPropertyValue(66570))
        .isEqualTo(66570);
    assertWithMessage("Character 0x1040b matches caselessly 0x1040b")
        .that(scanner.getPropertyValue(66571))
        .isEqualTo(66571);
    assertWithMessage("Character 0x1040c matches caselessly 0x1040c")
        .that(scanner.getPropertyValue(66572))
        .isEqualTo(66572);
    assertWithMessage("Character 0x1040d matches caselessly 0x1040d")
        .that(scanner.getPropertyValue(66573))
        .isEqualTo(66573);
    assertWithMessage("Character 0x1040e matches caselessly 0x1040e")
        .that(scanner.getPropertyValue(66574))
        .isEqualTo(66574);
    assertWithMessage("Character 0x1040f matches caselessly 0x1040f")
        .that(scanner.getPropertyValue(66575))
        .isEqualTo(66575);
    assertWithMessage("Character 0x10410 matches caselessly 0x10410")
        .that(scanner.getPropertyValue(66576))
        .isEqualTo(66576);
    assertWithMessage("Character 0x10411 matches caselessly 0x10411")
        .that(scanner.getPropertyValue(66577))
        .isEqualTo(66577);
    assertWithMessage("Character 0x10412 matches caselessly 0x10412")
        .that(scanner.getPropertyValue(66578))
        .isEqualTo(66578);
    assertWithMessage("Character 0x10413 matches caselessly 0x10413")
        .that(scanner.getPropertyValue(66579))
        .isEqualTo(66579);
    assertWithMessage("Character 0x10414 matches caselessly 0x10414")
        .that(scanner.getPropertyValue(66580))
        .isEqualTo(66580);
    assertWithMessage("Character 0x10415 matches caselessly 0x10415")
        .that(scanner.getPropertyValue(66581))
        .isEqualTo(66581);
    assertWithMessage("Character 0x10416 matches caselessly 0x10416")
        .that(scanner.getPropertyValue(66582))
        .isEqualTo(66582);
    assertWithMessage("Character 0x10417 matches caselessly 0x10417")
        .that(scanner.getPropertyValue(66583))
        .isEqualTo(66583);
    assertWithMessage("Character 0x10418 matches caselessly 0x10418")
        .that(scanner.getPropertyValue(66584))
        .isEqualTo(66584);
    assertWithMessage("Character 0x10419 matches caselessly 0x10419")
        .that(scanner.getPropertyValue(66585))
        .isEqualTo(66585);
    assertWithMessage("Character 0x1041a matches caselessly 0x1041a")
        .that(scanner.getPropertyValue(66586))
        .isEqualTo(66586);
    assertWithMessage("Character 0x1041b matches caselessly 0x1041b")
        .that(scanner.getPropertyValue(66587))
        .isEqualTo(66587);
    assertWithMessage("Character 0x1041c matches caselessly 0x1041c")
        .that(scanner.getPropertyValue(66588))
        .isEqualTo(66588);
    assertWithMessage("Character 0x1041d matches caselessly 0x1041d")
        .that(scanner.getPropertyValue(66589))
        .isEqualTo(66589);
    assertWithMessage("Character 0x1041e matches caselessly 0x1041e")
        .that(scanner.getPropertyValue(66590))
        .isEqualTo(66590);
    assertWithMessage("Character 0x1041f matches caselessly 0x1041f")
        .that(scanner.getPropertyValue(66591))
        .isEqualTo(66591);
    assertWithMessage("Character 0x10420 matches caselessly 0x10420")
        .that(scanner.getPropertyValue(66592))
        .isEqualTo(66592);
    assertWithMessage("Character 0x10421 matches caselessly 0x10421")
        .that(scanner.getPropertyValue(66593))
        .isEqualTo(66593);
    assertWithMessage("Character 0x10422 matches caselessly 0x10422")
        .that(scanner.getPropertyValue(66594))
        .isEqualTo(66594);
    assertWithMessage("Character 0x10423 matches caselessly 0x10423")
        .that(scanner.getPropertyValue(66595))
        .isEqualTo(66595);
    assertWithMessage("Character 0x10424 matches caselessly 0x10424")
        .that(scanner.getPropertyValue(66596))
        .isEqualTo(66596);
    assertWithMessage("Character 0x10425 matches caselessly 0x10425")
        .that(scanner.getPropertyValue(66597))
        .isEqualTo(66597);
    assertWithMessage("Character 0x10426 matches caselessly 0x10426")
        .that(scanner.getPropertyValue(66598))
        .isEqualTo(66598);
    assertWithMessage("Character 0x10427 matches caselessly 0x10427")
        .that(scanner.getPropertyValue(66599))
        .isEqualTo(66599);
    assertWithMessage("Character 0x10428 matches caselessly 0x10400")
        .that(scanner.getPropertyValue(66600))
        .isEqualTo(66560);
    assertWithMessage("Character 0x10429 matches caselessly 0x10401")
        .that(scanner.getPropertyValue(66601))
        .isEqualTo(66561);
    assertWithMessage("Character 0x1042a matches caselessly 0x10402")
        .that(scanner.getPropertyValue(66602))
        .isEqualTo(66562);
    assertWithMessage("Character 0x1042b matches caselessly 0x10403")
        .that(scanner.getPropertyValue(66603))
        .isEqualTo(66563);
    assertWithMessage("Character 0x1042c matches caselessly 0x10404")
        .that(scanner.getPropertyValue(66604))
        .isEqualTo(66564);
    assertWithMessage("Character 0x1042d matches caselessly 0x10405")
        .that(scanner.getPropertyValue(66605))
        .isEqualTo(66565);
    assertWithMessage("Character 0x1042e matches caselessly 0x10406")
        .that(scanner.getPropertyValue(66606))
        .isEqualTo(66566);
    assertWithMessage("Character 0x1042f matches caselessly 0x10407")
        .that(scanner.getPropertyValue(66607))
        .isEqualTo(66567);
    assertWithMessage("Character 0x10430 matches caselessly 0x10408")
        .that(scanner.getPropertyValue(66608))
        .isEqualTo(66568);
    assertWithMessage("Character 0x10431 matches caselessly 0x10409")
        .that(scanner.getPropertyValue(66609))
        .isEqualTo(66569);
    assertWithMessage("Character 0x10432 matches caselessly 0x1040a")
        .that(scanner.getPropertyValue(66610))
        .isEqualTo(66570);
    assertWithMessage("Character 0x10433 matches caselessly 0x1040b")
        .that(scanner.getPropertyValue(66611))
        .isEqualTo(66571);
    assertWithMessage("Character 0x10434 matches caselessly 0x1040c")
        .that(scanner.getPropertyValue(66612))
        .isEqualTo(66572);
    assertWithMessage("Character 0x10435 matches caselessly 0x1040d")
        .that(scanner.getPropertyValue(66613))
        .isEqualTo(66573);
    assertWithMessage("Character 0x10436 matches caselessly 0x1040e")
        .that(scanner.getPropertyValue(66614))
        .isEqualTo(66574);
    assertWithMessage("Character 0x10437 matches caselessly 0x1040f")
        .that(scanner.getPropertyValue(66615))
        .isEqualTo(66575);
    assertWithMessage("Character 0x10438 matches caselessly 0x10410")
        .that(scanner.getPropertyValue(66616))
        .isEqualTo(66576);
    assertWithMessage("Character 0x10439 matches caselessly 0x10411")
        .that(scanner.getPropertyValue(66617))
        .isEqualTo(66577);
    assertWithMessage("Character 0x1043a matches caselessly 0x10412")
        .that(scanner.getPropertyValue(66618))
        .isEqualTo(66578);
    assertWithMessage("Character 0x1043b matches caselessly 0x10413")
        .that(scanner.getPropertyValue(66619))
        .isEqualTo(66579);
    assertWithMessage("Character 0x1043c matches caselessly 0x10414")
        .that(scanner.getPropertyValue(66620))
        .isEqualTo(66580);
    assertWithMessage("Character 0x1043d matches caselessly 0x10415")
        .that(scanner.getPropertyValue(66621))
        .isEqualTo(66581);
    assertWithMessage("Character 0x1043e matches caselessly 0x10416")
        .that(scanner.getPropertyValue(66622))
        .isEqualTo(66582);
    assertWithMessage("Character 0x1043f matches caselessly 0x10417")
        .that(scanner.getPropertyValue(66623))
        .isEqualTo(66583);
    assertWithMessage("Character 0x10440 matches caselessly 0x10418")
        .that(scanner.getPropertyValue(66624))
        .isEqualTo(66584);
    assertWithMessage("Character 0x10441 matches caselessly 0x10419")
        .that(scanner.getPropertyValue(66625))
        .isEqualTo(66585);
    assertWithMessage("Character 0x10442 matches caselessly 0x1041a")
        .that(scanner.getPropertyValue(66626))
        .isEqualTo(66586);
    assertWithMessage("Character 0x10443 matches caselessly 0x1041b")
        .that(scanner.getPropertyValue(66627))
        .isEqualTo(66587);
    assertWithMessage("Character 0x10444 matches caselessly 0x1041c")
        .that(scanner.getPropertyValue(66628))
        .isEqualTo(66588);
    assertWithMessage("Character 0x10445 matches caselessly 0x1041d")
        .that(scanner.getPropertyValue(66629))
        .isEqualTo(66589);
    assertWithMessage("Character 0x10446 matches caselessly 0x1041e")
        .that(scanner.getPropertyValue(66630))
        .isEqualTo(66590);
    assertWithMessage("Character 0x10447 matches caselessly 0x1041f")
        .that(scanner.getPropertyValue(66631))
        .isEqualTo(66591);
    assertWithMessage("Character 0x10448 matches caselessly 0x10420")
        .that(scanner.getPropertyValue(66632))
        .isEqualTo(66592);
    assertWithMessage("Character 0x10449 matches caselessly 0x10421")
        .that(scanner.getPropertyValue(66633))
        .isEqualTo(66593);
    assertWithMessage("Character 0x1044a matches caselessly 0x10422")
        .that(scanner.getPropertyValue(66634))
        .isEqualTo(66594);
    assertWithMessage("Character 0x1044b matches caselessly 0x10423")
        .that(scanner.getPropertyValue(66635))
        .isEqualTo(66595);
    assertWithMessage("Character 0x1044c matches caselessly 0x10424")
        .that(scanner.getPropertyValue(66636))
        .isEqualTo(66596);
    assertWithMessage("Character 0x1044d matches caselessly 0x10425")
        .that(scanner.getPropertyValue(66637))
        .isEqualTo(66597);
    assertWithMessage("Character 0x1044e matches caselessly 0x10426")
        .that(scanner.getPropertyValue(66638))
        .isEqualTo(66598);
    assertWithMessage("Character 0x1044f matches caselessly 0x10427")
        .that(scanner.getPropertyValue(66639))
        .isEqualTo(66599);
  }
}
