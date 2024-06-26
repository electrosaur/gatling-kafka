
package io.github.amerousful.kafka.internal

import io.gatling.commons.validation.{SuccessWrapper, Validation, safely}
import io.gatling.core.session.{Expression, Session => ScalaSession}
import io.gatling.javaapi.core.internal.Expressions._
import io.gatling.javaapi.core.internal.JavaExpression
import io.gatling.javaapi.core.{CheckBuilder, Session}
import io.github.amerousful.kafka.KafkaResponseMessage
import io.github.amerousful.kafka.javaapi.OnlyConsumeActionBuilder

import java.util.{function => juf}
import java.{lang => jl, util => ju}

object ScalaKafkaOnlyConsumeBuilderConditions {
  def untyped(context: io.github.amerousful.kafka.request.OnlyConsumeDslBuilder, condition: String): Untyped =
    new Untyped(context, toBooleanExpression(condition))

  def untyped(context: io.github.amerousful.kafka.request.OnlyConsumeDslBuilder, condition: JavaExpression[jl.Boolean]): Untyped =
    new Untyped(context, javaBooleanFunctionToExpression(condition))

  final class Untyped(context: io.github.amerousful.kafka.request.OnlyConsumeDslBuilder, condition: Expression[Boolean]) {
    def thenChecks(thenChecks: ju.List[CheckBuilder]): OnlyConsumeActionBuilder =
      new OnlyConsumeActionBuilder(context.checkIf(condition)(KafkaChecks.toScalaChecks(thenChecks): _*))
  }

  def typed(context: io.github.amerousful.kafka.request.OnlyConsumeDslBuilder, condition: juf.BiFunction[KafkaResponseMessage, Session, jl.Boolean]): Typed =
    new Typed(context, (u, session) => safely()(condition.apply(u, new Session(session)).booleanValue.success))

  final class Typed(context: io.github.amerousful.kafka.request.OnlyConsumeDslBuilder, condition: (KafkaResponseMessage, ScalaSession) => Validation[Boolean]) {
    def then_(thenChecks: ju.List[CheckBuilder]): OnlyConsumeActionBuilder =
      new OnlyConsumeActionBuilder(context.checkIf(condition)(KafkaChecks.toScalaChecks(thenChecks): _*))
  }
}
