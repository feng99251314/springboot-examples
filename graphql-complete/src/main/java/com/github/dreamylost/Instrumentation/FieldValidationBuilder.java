package com.github.dreamylost.Instrumentation;

import graphql.execution.ExecutionPath;
import graphql.execution.instrumentation.fieldvalidation.FieldValidation;
import graphql.execution.instrumentation.fieldvalidation.FieldValidationInstrumentation;
import graphql.execution.instrumentation.fieldvalidation.SimpleFieldValidation;

import java.util.Optional;

/**
 * @author liguobin@growingio.com
 * @version 1.0, 2020/3/31
 */
public class FieldValidationBuilder {

    public static FieldValidationInstrumentation builder() {
        //遇到是human的请求，强制验证id长度不能小于4
        /**
         * 否则返回错误信息
         * {
         *     "errors": [
         *         {
         *             "message": "Invalid id length",
         *             "locations": [
         *                 {
         *                     "line": 2,
         *                     "column": 3
         *                 }
         *             ],
         *             "path": [
         *                 "human"
         *             ]
         *         }
         *     ]
         * }
         */

        ExecutionPath fieldPath = ExecutionPath.parse("/human");
        FieldValidation fieldValidation = new SimpleFieldValidation()
                .addRule(fieldPath, (fieldAndArguments, environment) -> {
                    String nameArg = fieldAndArguments.getArgumentValue("id");
                    if (nameArg.length() < 4) {
                        return Optional.of(environment.mkError("Invalid id length", fieldAndArguments));
                    }
                    return Optional.empty();
                });

        return new FieldValidationInstrumentation(fieldValidation);
    }
}
