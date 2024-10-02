package com.eternalcode.combat.fight.drop;

public interface DropService {

    DropResult modify(DropType dropType, Drop drop);

    void registerModifier(DropModifier dropModifier);
}
