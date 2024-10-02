package com.eternalcode.combat.fight.drop;

public interface DropModifier {

    DropType getDropType();

    DropResult modifyDrop(Drop drop);

}
