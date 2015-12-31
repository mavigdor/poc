package com.gigaspaces.poc.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moran on 12/28/15.
 */
public class MoneyTransferJourney extends Journey {

    public MoneyTransferJourney() {
        List<Instruction> instructions = new ArrayList<Instruction>(2);
        instructions.add(new MoneyTransferInstruction());
        //instructions.add(new MoneyTransferInstruction());
        setInstructions(instructions);
    }
}
