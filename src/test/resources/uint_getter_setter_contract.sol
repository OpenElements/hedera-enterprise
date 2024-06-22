// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.4.16 <0.9.0;

contract SimpleStorage {
    int storedData;

    function set(int x) public {
        storedData = x;
    }

    function get() public view returns (int) {
        return storedData;
    }
}