package de.redsix.dmncheck.validators;

import de.redsix.dmncheck.result.ValidationResult;
import de.redsix.dmncheck.validators.core.SimpleValidator;
import org.camunda.bpm.model.dmn.BuiltinAggregator;
import org.camunda.bpm.model.dmn.HitPolicy;
import org.camunda.bpm.model.dmn.instance.DecisionTable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class AggregationValidator extends SimpleValidator<DecisionTable> {

    @Override
    public boolean isApplicable(DecisionTable decisionTable) {
        return !HitPolicy.COLLECT.equals(decisionTable.getHitPolicy());
    }

    @Override
    public List<ValidationResult> validate(DecisionTable decisionTable) {
        final BuiltinAggregator builtinAggregator = decisionTable.getAggregation();
        if (Objects.nonNull(builtinAggregator)) {
            return Collections.singletonList(ValidationResult.Builder.with($ -> {
                $.message = "Aggregations are only valid with HitPolicy " + HitPolicy.COLLECT;
                $.element = decisionTable;
            }).build());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Class<DecisionTable> getClassUnderValidation() {
        return DecisionTable.class;
    }
}
