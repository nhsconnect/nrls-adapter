/* Copyright 2018 NHS Digital

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. */

package nrls.adapter.helpers;

public final class Validators {
    
    public static boolean nhsNumberValid(String nhsNumber) {

        // NHS numbers should only contain 10 numeric values
        if (null == nhsNumber || !nhsNumber.matches("[0-9]{10}")) {
            return false;
        }

        // Modulus 11 Checked
        String[] nhsNumberDigits = nhsNumber.split("(?!^)");

        int result = Integer.parseInt(nhsNumberDigits[0]) * 10
                + Integer.parseInt(nhsNumberDigits[1]) * 9
                + Integer.parseInt(nhsNumberDigits[2]) * 8
                + Integer.parseInt(nhsNumberDigits[3]) * 7
                + Integer.parseInt(nhsNumberDigits[4]) * 6
                + Integer.parseInt(nhsNumberDigits[5]) * 5
                + Integer.parseInt(nhsNumberDigits[6]) * 4
                + Integer.parseInt(nhsNumberDigits[7]) * 3
                + Integer.parseInt(nhsNumberDigits[8]) * 2;
        result = (11 - (result % 11)) % 11;

        return result == Integer.parseInt(nhsNumberDigits[9]);
    }
}
