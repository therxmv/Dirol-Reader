package com.therxmv.dirolreader.data.source.locale.message.models

import com.therxmv.dirolreader.data.utils.EntityMapper
import com.therxmv.dirolreader.domain.models.MessageModel

class MessageLocaleMapper: EntityMapper<MessageEntity, MessageModel> {
    override fun mapFromEntity(entity: MessageEntity): MessageModel {
        return MessageModel(
            entity.id,
            entity.messageThreadId,
            entity.channelId,
            entity.date,
            entity.text,
            entity.photoPath,
            entity.isViewed,
            entity.isLast,
        )
    }

    override fun mapToEntity(domainModel: MessageModel): MessageEntity {
        return MessageEntity(
            domainModel.id,
            domainModel.messageThreadId,
            domainModel.channelId,
            domainModel.date,
            domainModel.text,
            domainModel.photoPath,
            domainModel.isViewed,
            domainModel.isLast,
        )
    }

}