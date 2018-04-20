package de.redsix.dmncheck.validators;

import de.redsix.dmncheck.result.ValidationResult;
import de.redsix.dmncheck.result.ValidationResultType;
import de.redsix.dmncheck.validators.util.WithDecisionTable;
import org.camunda.bpm.model.dmn.BuiltinAggregator;
import org.camunda.bpm.model.dmn.HitPolicy;
import org.camunda.bpm.model.dmn.instance.Output;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AggregationOutputTypeValidatorTest extends WithDecisionTable {

    private final AggregationOutputTypeValidator testee = new AggregationOutputTypeValidator();

    @Test
    void shouldErrorOnStringOutputWithMaxAggregator() {
        decisionTable.setHitPolicy(HitPolicy.COLLECT);
        decisionTable.setAggregation(BuiltinAggregator.MAX);
        final Output output = modelInstance.newInstance(Output.class);
        output.setTypeRef("string");
        decisionTable.getOutputs().add(output);

        final List<ValidationResult> validationResults = testee.apply(modelInstance);

        assertEquals(1, validationResults.size());
        final ValidationResult validationResult = validationResults.get(0);
        assertAll(
                () -> assertEquals("Aggregations MAX, MIN, SUM are only valid with numeric output types", validationResult.getMessage()),
                () -> assertEquals(output, validationResult.getElement()),
                () -> assertEquals(ValidationResultType.ERROR, validationResult.getValidationResultType())
        );
    }

    @Test
    void shouldWarnOnEmptyOutputtypeWithAggregation() {
        decisionTable.setHitPolicy(HitPolicy.COLLECT);
        decisionTable.setAggregation(BuiltinAggregator.MAX);
        final Output output = modelInstance.newInstance(Output.class);
        decisionTable.getOutputs().add(output);

        final List<ValidationResult> validationResults = testee.apply(modelInstance);

        assertEquals(1, validationResults.size());
        final ValidationResult validationResult = validationResults.get(0);
        assertAll(
                () -> assertEquals("An aggregation is used but no output type is defined", validationResult.getMessage()),
                () -> assertEquals(output, validationResult.getElement()),
                () -> assertEquals(ValidationResultType.WARNING, validationResult.getValidationResultType())
        );
    }

    @Test
    void shouldAllowAggregatorMaxWithIntegerOutputs() {
        decisionTable.setHitPolicy(HitPolicy.COLLECT);
        decisionTable.setAggregation(BuiltinAggregator.MAX);
        final Output output = modelInstance.newInstance(Output.class);
        output.setTypeRef("integer");
        decisionTable.getOutputs().add(output);

        final List<ValidationResult> validationResults = testee.apply(modelInstance);

        assertTrue(validationResults.isEmpty());
    }
}
