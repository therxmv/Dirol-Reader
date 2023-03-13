package com.therxmv.dirolreader.data.source.locale.channel.models

import com.therxmv.dirolreader.data.utils.EntityMapper
import com.therxmv.dirolreader.domain.models.ChannelModel

class ChannelLocaleMapper: EntityMapper<ChannelEntity, ChannelModel> {
    override fun mapFromEntity(entity: ChannelEntity): ChannelModel {
        return ChannelModel(
            entity.id,
            entity.title,
            entity.avatarId,
            entity.rating,
        )
    }

    override fun mapToEntity(domainModel: ChannelModel): ChannelEntity {
        return ChannelEntity(
            domainModel.id,
            domainModel.title,
            domainModel.avatarId,
            domainModel.rating,
        )
    }
}