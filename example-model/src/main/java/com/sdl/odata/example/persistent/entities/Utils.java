/**
 * Copyright (c) 2015 SDL Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sdl.odata.example.persistent.entities;

import java.util.UUID;

/**
 * Created by hrawat on 8/24/15.
 * 
 * TODO: Delete this class and see if we can share this class through a 
 * common artifact with Identity service. 
 */
public class Utils {


    static final int LONG_ENC_STR_LEN = 11;
    public static final int UUID_ENC_STR_LEN = LONG_ENC_STR_LEN*2;

    private static final Character[] printableChars =  {
            '0' , '1' , '2' , '3' , '4' , '5' ,
            '6' , '7' , '8' , '9' , 'a' , 'b' ,
            'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
            'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
            'o' , 'p' , 'q' , 'r' , 's' , 't' ,
            'u' , 'v' , 'w' , 'x' , 'y' , 'z',
            'A' , 'B' ,
            'C' , 'D' , 'E' , 'F' , 'G' , 'H' ,
            'I' , 'J' , 'K' , 'L' , 'M' , 'N' ,
            'O' , 'P' , 'Q' , 'R' , 'S' , 'T' ,
            'U' , 'V' , 'W' , 'X' , 'Y' , 'Z'};

    private Utils() {}

    public static String uuidString() {
        UUID uuid = UUID.randomUUID();
        return uuidToString(uuid);
    }



    public static String uuidToString(UUID uuid) {
        StringBuffer buffer = new StringBuffer();
        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();
        buffer.append(longToString(mostSigBits));
        buffer.append(longToString(leastSigBits));
        return buffer.toString();
    }

     static String longToString(long value) {
        StringBuffer buf = new StringBuffer();


         for (int i=0; i < LONG_ENC_STR_LEN; i++) {
             int remainder = (int) (value % printableChars.length);
             buf.insert(0, printableChars[Math.abs(remainder)]);
             value = value / printableChars.length;
             if ((i ==0) && (value < 0)) {
                value = value*2;
             }

         }


        return buf.toString();
    }
}
