package io.github.amerousful.kafka.client

import io.github.amerousful.kafka.Predef.ProtoBufScalaPB
import io.github.amerousful.kafka.request.ProtoAttributes
import io.github.amerousful.kafka.utils.serdes.protobuf.KafkaScalaPBSerde
import scalapb.GeneratedMessage

object SerdeScalaPB {

  def create[ScalaPB <: GeneratedMessage, JavaPB <: com.google.protobuf.Message](companion: ProtoBufScalaPB[ScalaPB, JavaPB])= {
    val javaPB = companion.toJavaProto(companion.defaultInstance).getClass
    val serde = new KafkaScalaPBSerde[ScalaPB, JavaPB](companion)

    ProtoAttributes(serde.deserializer(), javaPB)
  }

}
