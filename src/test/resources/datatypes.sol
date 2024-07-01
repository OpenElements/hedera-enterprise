// SPDX-License-Identifier: GPL-3.0

pragma solidity >=0.7.0 <0.9.0;
contract Datatypes {

    function checkString(string memory data) public pure returns (string memory) {
        return data;
    }

    function checkAddress(address data) public pure returns (address) {
        return data;
    }

    function checkInt8(int8 data) public pure returns (int8) {
        return data;
    }

    function checkUint8(uint8 data) public pure returns (uint8) {
        return data;
    }

}