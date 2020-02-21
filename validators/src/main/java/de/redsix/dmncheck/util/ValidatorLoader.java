package de.redsix.dmncheck.util;

import de.redsix.dmncheck.validators.core.Validator;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class ValidatorLoader {

    private static final String VALIDATOR_PACKAGE = "de.redsix.dmncheck.validators";
    private static final String VALIDATOR_CORE_PACKAGE = "de.redsix.dmncheck.validators.core";

    private ValidatorLoader() {

    }

    public static List<Validator> getValidators() {
        return getValidators(null, null);
    }

    public static List<Validator> getValidators(final String @Nullable [] packages, final String @Nullable [] classes) {
        final ScanResult scanResult = new ClassGraph()
                .whitelistClasses(Validator.class.getName())
                .whitelistPackages(VALIDATOR_CORE_PACKAGE)
                .whitelistPackagesNonRecursive(packages == null ? new String[] {VALIDATOR_PACKAGE, VALIDATOR_PACKAGE + ".core"} : packages)
                .whitelistClasses(classes == null ? new String[]{} : classes)
                .scan();

        final ClassInfoList allValidatorClasses = scanResult.getClassesImplementing(Validator.class.getName());

        return allValidatorClasses.loadClasses(Validator.class)
                                  .stream()
                                  .filter(validatorClass -> !Modifier.isAbstract(validatorClass.getModifiers()))
                                  .filter(validatorClass -> !Modifier.isInterface(validatorClass.getModifiers()))
                                  .map(ValidatorLoader::instantiateValidator)
                                  .collect(Collectors.toList());
    }

    private static Validator instantiateValidator(final Class<? extends Validator> validator) {
        try {
            return validator.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Failed to load validator " + validator, e);
        }
    }

}
