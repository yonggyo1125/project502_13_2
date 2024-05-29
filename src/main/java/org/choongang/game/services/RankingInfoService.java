package org.choongang.game.services;

import org.choongang.game.entities.Rank;
import org.choongang.game.mapper.PointLogMapper;
import org.choongang.global.Service;

import java.util.List;

public class RankingInfoService implements Service<List<Rank>> {

    private final PointLogMapper mapper;

    public RankingInfoService(PointLogMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Rank> process() {

        return mapper.getRank();
    }
}
