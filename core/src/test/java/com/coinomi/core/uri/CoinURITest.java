package com.coinomi.core.uri;

/*
 * Copyright 2012, 2014 the original author or authors.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import com.coinomi.core.coins.BitcoinMain;
import com.coinomi.core.coins.BitcoinTest;
import com.coinomi.core.coins.DarkcoinMain;
import com.coinomi.core.coins.DogecoinMain;
import com.coinomi.core.coins.LitecoinMain;
import com.coinomi.core.coins.PeercoinMain;
import com.coinomi.core.util.GenericUtils;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;

import org.junit.Test;

import java.io.UnsupportedEncodingException;


import static com.coinomi.core.util.GenericUtils.parseCoin;
import static org.junit.Assert.*;

public class CoinURITest {
    private CoinURI testObject = null;

    private static final String MAINNET_GOOD_ADDRESS = "1KzTSfqjF2iKCduwz59nv2uqh1W2JsTxZH";

    @Test
    public void testConvertToCoinURI() throws Exception {
        Address goodAddress = new Address(BitcoinMain.get(), MAINNET_GOOD_ADDRESS);
        
        // simple example
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?amount=12.34&label=Hello&message=AMessage", CoinURI.convertToCoinURI(goodAddress, parseCoin(BitcoinMain.get(), "12.34"), "Hello", "AMessage"));
        
        // example with spaces, ampersand and plus
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?amount=12.34&label=Hello%20World&message=Mess%20%26%20age%20%2B%20hope", CoinURI.convertToCoinURI(goodAddress, parseCoin(BitcoinMain.get(), "12.34"), "Hello World", "Mess & age + hope"));

        // no amount, label present, message present
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?label=Hello&message=glory", CoinURI.convertToCoinURI(goodAddress, null, "Hello", "glory"));
        
        // amount present, no label, message present
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?amount=0.1&message=glory", CoinURI.convertToCoinURI(goodAddress, parseCoin(BitcoinMain.get(), "0.1"), null, "glory"));
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?amount=0.1&message=glory", CoinURI.convertToCoinURI(goodAddress, parseCoin(BitcoinMain.get(), "0.1"), "", "glory"));

        // amount present, label present, no message
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?amount=12.34&label=Hello", CoinURI.convertToCoinURI(goodAddress, parseCoin(BitcoinMain.get(), "12.34"), "Hello", null));
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?amount=12.34&label=Hello", CoinURI.convertToCoinURI(goodAddress, parseCoin(BitcoinMain.get(), "12.34"), "Hello", ""));
              
        // amount present, no label, no message
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?amount=1000", CoinURI.convertToCoinURI(goodAddress, parseCoin(BitcoinMain.get(), "1000"), null, null));
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?amount=1000", CoinURI.convertToCoinURI(goodAddress, parseCoin(BitcoinMain.get(), "1000"), "", ""));
        
        // no amount, label present, no message
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?label=Hello", CoinURI.convertToCoinURI(goodAddress, null, "Hello", null));
        
        // no amount, no label, message present
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?message=Agatha", CoinURI.convertToCoinURI(goodAddress, null, null, "Agatha"));
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS + "?message=Agatha", CoinURI.convertToCoinURI(goodAddress, null, "", "Agatha"));
      
        // no amount, no label, no message
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS, CoinURI.convertToCoinURI(goodAddress, null, null, null));
        assertEquals("bitcoin:" + MAINNET_GOOD_ADDRESS, CoinURI.convertToCoinURI(goodAddress, null, "", ""));
    }

    @Test
    public void testAltChainsConvertToCoinURI() throws Exception {
        byte[] hash160 = new Address(BitcoinMain.get(), MAINNET_GOOD_ADDRESS).getHash160();
        String goodAddressStr;
        Address goodAddress;

        // Litecoin
        goodAddress = new Address(LitecoinMain.get(), hash160);
        goodAddressStr = goodAddress.toString();
        assertEquals("litecoin:" + goodAddressStr + "?amount=12.34&label=Hello&message=AMessage", CoinURI.convertToCoinURI(goodAddress, parseCoin(LitecoinMain.get(), "12.34"), "Hello", "AMessage"));

        // Dogecoin
        goodAddress = new Address(DogecoinMain.get(), hash160);
        goodAddressStr = goodAddress.toString();
        assertEquals("dogecoin:" + goodAddressStr + "?amount=12.34&label=Hello&message=AMessage", CoinURI.convertToCoinURI(goodAddress, parseCoin(DogecoinMain.get(), "12.34"), "Hello", "AMessage"));

        // Peercoin
        goodAddress = new Address(PeercoinMain.get(), hash160);
        goodAddressStr = goodAddress.toString();
        assertEquals("peercoin:" + goodAddressStr + "?amount=12.34&label=Hello&message=AMessage", CoinURI.convertToCoinURI(goodAddress, parseCoin(PeercoinMain.get(), "12.34"), "Hello", "AMessage"));

        // Darkcoin
        goodAddress = new Address(DarkcoinMain.get(), hash160);
        goodAddressStr = goodAddress.toString();
        assertEquals("darkcoin:" + goodAddressStr + "?amount=12.34&label=Hello&message=AMessage", CoinURI.convertToCoinURI(goodAddress, parseCoin(DarkcoinMain.get(), "12.34"), "Hello", "AMessage"));
    }

    @Test
    public void testAltChainsGoodAmount() throws Exception {
        byte[] hash160 = new Address(BitcoinMain.get(), MAINNET_GOOD_ADDRESS).getHash160();
        String goodAddressStr;
        Address goodAddress;

        // Litecoin
        goodAddress = new Address(LitecoinMain.get(), hash160);
        goodAddressStr = goodAddress.toString();
        testObject = new CoinURI(LitecoinMain.get(), "litecoin:" + goodAddressStr + "?amount=12.34");
        assertEquals("12.34", GenericUtils.formatCoinValue(LitecoinMain.get(), testObject.getAmount()));

        // Dogecoin
        goodAddress = new Address(DogecoinMain.get(), hash160);
        goodAddressStr = goodAddress.toString();
        testObject = new CoinURI(DogecoinMain.get(), "dogecoin:" + goodAddressStr + "?amount=12.34");
        assertEquals("12.34", GenericUtils.formatCoinValue(DogecoinMain.get(), testObject.getAmount()));

        // Peercoin
        goodAddress = new Address(PeercoinMain.get(), hash160);
        goodAddressStr = goodAddress.toString();
        testObject = new CoinURI(PeercoinMain.get(), "peercoin:" + goodAddressStr + "?amount=12.34");
        assertEquals("12.34", GenericUtils.formatCoinValue(PeercoinMain.get(), testObject.getAmount()));

        // Darkcoin
        goodAddress = new Address(DarkcoinMain.get(), hash160);
        goodAddressStr = goodAddress.toString();
        testObject = new CoinURI(DarkcoinMain.get(), "darkcoin:" + goodAddressStr + "?amount=12.34");
        assertEquals("12.34", GenericUtils.formatCoinValue(DarkcoinMain.get(), testObject.getAmount()));
    }

    @Test
    public void testJustAddressNoUri() throws Exception {
        byte[] hash160 = new Address(BitcoinMain.get(), MAINNET_GOOD_ADDRESS).getHash160();
        String goodAddressStr;
        Address goodAddress;

        testObject = new CoinURI(MAINNET_GOOD_ADDRESS);
        assertNotNull(testObject);
        assertNull("Unexpected amount", testObject.getAmount());
        assertNull("Unexpected amount", testObject.getAmount());
        assertNull("Unexpected label", testObject.getLabel());
        assertEquals("Unexpected label", MAINNET_GOOD_ADDRESS, testObject.getAddress().toString());
    }

    @Test
    public void testGood_Simple() throws CoinURIParseException {
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS);
        assertNotNull(testObject);
        assertNull("Unexpected amount", testObject.getAmount());
        assertNull("Unexpected label", testObject.getLabel());
        assertEquals("Unexpected label", 20, testObject.getAddress().getHash160().length);
    }

    /**
     * Test a broken URI (bad scheme)
     */
    @Test
    public void testBad_Scheme() {
        try {
            testObject = new CoinURI(BitcoinMain.get(), "blimpcoin:" + MAINNET_GOOD_ADDRESS);
            fail("Expecting BitcoinURIParseException");
        } catch (CoinURIParseException e) {
        }
    }

    /**
     * Test a broken URI (bad syntax)
     */
    @Test
    public void testBad_BadSyntax() {
        // Various illegal characters
        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + "|" + MAINNET_GOOD_ADDRESS);
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("Bad URI syntax"));
        }

        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS + "\\");
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("Bad URI syntax"));
        }

        // Separator without field
        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":");
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("Bad URI syntax"));
        }
    }

    /**
     * Test a broken URI (missing address)
     */
    @Test
    public void testBad_Address() {
        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme());
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
        }
    }

    /**
     * Test a broken URI (bad address type)
     */
    @Test
    public void testBad_IncorrectAddressType() {
        try {
            testObject = new CoinURI(BitcoinTest.get(), BitcoinTest.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS);
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("Bad address"));
        }
    }

    /**
     * Handles a simple amount
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     */
    @Test
    public void testGood_Amount() throws CoinURIParseException {
        // Test the decimal parsing
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?amount=6543210.12345678");
        assertEquals("654321012345678", testObject.getAmount().toString());

        // Test the decimal parsing
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?amount=.12345678");
        assertEquals("12345678", testObject.getAmount().toString());

        // Test the integer parsing
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?amount=6543210");
        assertEquals("654321000000000", testObject.getAmount().toString());
    }

    /**
     * Handles a simple label
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     */
    @Test
    public void testGood_Label() throws CoinURIParseException {
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?label=Hello%20World");
        assertEquals("Hello World", testObject.getLabel());
    }

    /**
     * Handles a simple label with an embedded ampersand and plus
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testGood_LabelWithAmpersandAndPlus() throws Exception {
        String testString = "Hello Earth & Mars + Venus";
        String encodedLabel = CoinURI.encodeURLString(testString);
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS + "?label="
                + encodedLabel);
        assertEquals(testString, testObject.getLabel());
    }

    /**
     * Handles a Russian label (Unicode test)
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testGood_LabelWithRussian() throws Exception {
        // Moscow in Russian in Cyrillic
        String moscowString = "\u041c\u043e\u0441\u043a\u0432\u0430";
        String encodedLabel = CoinURI.encodeURLString(moscowString); 
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS + "?label="
                + encodedLabel);
        assertEquals(moscowString, testObject.getLabel());
    }

    /**
     * Handles a simple message
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     */
    @Test
    public void testGood_Message() throws CoinURIParseException {
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?message=Hello%20World");
        assertEquals("Hello World", testObject.getMessage());
    }

    /**
     * Handles various well-formed combinations
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     */
    @Test
    public void testGood_Combinations() throws CoinURIParseException {
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?amount=6543210&label=Hello%20World&message=Be%20well");
        assertEquals(
                "CoinURI['amount'='654321000000000','label'='Hello World','message'='Be well','address'='1KzTSfqjF2iKCduwz59nv2uqh1W2JsTxZH']",
                testObject.toString());
    }

    /**
     * Handles a badly formatted amount field
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     */
    @Test
    public void testBad_Amount() throws CoinURIParseException {
        // Missing
        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                    + "?amount=");
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("amount"));
        }

        // Non-decimal (BIP 21)
        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                    + "?amount=12X4");
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("amount"));
        }
    }

    @Test
    public void testEmpty_Label() throws CoinURIParseException {
        assertNull(new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?label=").getLabel());
    }

    @Test
    public void testEmpty_Message() throws CoinURIParseException {
        assertNull(new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?message=").getMessage());
    }

    /**
     * Handles duplicated fields (sneaky address overwrite attack)
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     */
    @Test
    public void testBad_Duplicated() throws CoinURIParseException {
        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                    + "?address=aardvark");
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("address"));
        }
    }

    @Test
    public void testGood_ManyEquals() throws CoinURIParseException {
        assertEquals("aardvark=zebra", new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":"
                + MAINNET_GOOD_ADDRESS + "?label=aardvark=zebra").getLabel());
    }

    /**
     * Handles case when there are too many question marks
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     */
    @Test
    public void testBad_TooManyQuestionMarks() throws CoinURIParseException {
        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                    + "?label=aardvark?message=zebra");
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("Too many question marks"));
        }
    }
    
    /**
     * Handles unknown fields (required and not required)
     * 
     * @throws CoinURIParseException
     *             If something goes wrong
     */
    @Test
    public void testUnknown() throws CoinURIParseException {
        // Unknown not required field
        testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?aardvark=true");
        assertEquals("CoinURI['aardvark'='true','address'='1KzTSfqjF2iKCduwz59nv2uqh1W2JsTxZH']", testObject.toString());

        assertEquals("true", (String) testObject.getParameterByName("aardvark"));

        // Unknown not required field (isolated)
        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                    + "?aardvark");
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("no separator"));
        }

        // Unknown and required field
        try {
            testObject = new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                    + "?req-aardvark=true");
            fail("Expecting CoinURIParseException");
        } catch (CoinURIParseException e) {
            assertTrue(e.getMessage().contains("req-aardvark"));
        }
    }

    @Test
    public void brokenURIs() throws CoinURIParseException {
        // Check we can parse the incorrectly formatted URIs produced by blockchain.info and its iPhone app.
        String str = "bitcoin://1KzTSfqjF2iKCduwz59nv2uqh1W2JsTxZH?amount=0.01000000";
        CoinURI uri = new CoinURI(str);
        assertEquals("1KzTSfqjF2iKCduwz59nv2uqh1W2JsTxZH", uri.getAddress().toString());
        assertEquals(Coin.CENT, uri.getAmount());
    }

    @Test(expected = CoinURIParseException.class)
    public void testBad_AmountTooPrecise() throws CoinURIParseException {
        new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?amount=0.123456789");
    }

    @Test(expected = CoinURIParseException.class)
    public void testBad_NegativeAmount() throws CoinURIParseException {
        new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
                + "?amount=-1");
    }

//    @Test(expected = CoinURIParseException.class)
//    public void testBad_TooLargeAmount() throws CoinURIParseException {
//        new CoinURI(BitcoinMain.get(), BitcoinMain.get().getUriScheme() + ":" + MAINNET_GOOD_ADDRESS
//                + "?amount=100000000");
//    }

    @Test
    public void testPaymentProtocolReq() throws Exception {
        // Non-backwards compatible form ...
        CoinURI uri = new CoinURI(BitcoinTest.get(), "bitcoin:?r=https%3A%2F%2Fbitcoincore.org%2F%7Egavin%2Ff.php%3Fh%3Db0f02e7cea67f168e25ec9b9f9d584f9");
        assertEquals("https://bitcoincore.org/~gavin/f.php?h=b0f02e7cea67f168e25ec9b9f9d584f9", uri.getPaymentRequestUrl());
        assertNull(uri.getAddress());
    }
}
