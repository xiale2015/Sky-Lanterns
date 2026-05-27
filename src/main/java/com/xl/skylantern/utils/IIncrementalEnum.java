package com.xl.skylantern.utils;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public interface IIncrementalEnum<TYPE extends Enum<TYPE> & IIncrementalEnum<TYPE>> {

    @Nonnull
    default TYPE getNext(@Nonnull Predicate<TYPE> isValid) {
        TYPE next = byIndex(ordinal() + 1);
        while (!isValid.test(next)) {
            if (next == this) {
                return next;
            }
            next = byIndex(next.ordinal() + 1);
        }
        return next;
    }

    @Nonnull
    default TYPE getPrevious(@Nonnull Predicate<TYPE> isValid) {
        TYPE previous = byIndex(ordinal() - 1);
        while (!isValid.test(previous)) {
            if (previous == this) {
                return previous;
            }
            previous = byIndex(previous.ordinal() - 1);
        }
        return previous;
    }

    @Nonnull
    TYPE byIndex(int index);

    int ordinal();

    @Nonnull
    default TYPE getNext() {
        return getNext(type -> true);
    }

    @Nonnull
    default TYPE getPrevious() {
        return getPrevious(type -> true);
    }

    @Nonnull
    default TYPE adjust(int shift) {
        return shift == 0 ? (TYPE) this : byIndex(ordinal() + shift);
    }

    @Nonnull
    default TYPE adjust(int shift, Predicate<TYPE> isValid) {
        TYPE result = (TYPE) this;
        while (shift < 0) {
            shift++;
            result = result.getPrevious(isValid);
        }
        while (shift > 0) {
            shift--;
            result = result.getNext(isValid);
        }
        return result;
    }
}
