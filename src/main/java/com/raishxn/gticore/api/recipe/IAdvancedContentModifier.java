package com.raishxn.gticore.api.recipe;

import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;

public interface IAdvancedContentModifier {

    void setDivision(long numerator, long denominator);

    static ContentModifier preciseDivision(long numerator, long denominator) {
        var modifier = new ContentModifier(0, 0); // Cria o objeto original
        ((IAdvancedContentModifier) (Object) modifier).setDivision(numerator, denominator);
        return modifier;
    }
}
