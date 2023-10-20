package com.carlocodes.social.mappers;

import com.carlocodes.social.dtos.BuddyDto;
import com.carlocodes.social.entities.Buddy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BuddyMapper {
    BuddyMapper INSTANCE = Mappers.getMapper(BuddyMapper.class);

    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "receiver", target = "receiver")
    BuddyDto mapToDto(Buddy buddy);
    List<BuddyDto> mapToDtos(List<Buddy> buddies);
}
