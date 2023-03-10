package com.therxmv.dirolreader.data.source.locale.model

import com.therxmv.dirolreader.data.utils.EntityMapper
import com.therxmv.dirolreader.domain.models.ChannelModel

class ChannelLocaleMapper: EntityMapper<ChannelEntity, ChannelModel> {
    override fun mapFromEntity(entity: ChannelEntity): ChannelModel {
        return ChannelModel(
            entity.id,
            entity.avatarPath,
            entity.channelName,
            entity.lastMessageText,
            entity.lastMessageDate,
            entity.photoPath,
            entity.rating,
        )
    }

    override fun mapToEntity(domainModel: ChannelModel): ChannelEntity {
        return ChannelEntity(
            domainModel.id,
            domainModel.avatarPath,
            domainModel.channelName,
            domainModel.lastMessageText,
            domainModel.lastMessageDate,
            domainModel.photoPath,
            domainModel.rating,
        )
    }
}