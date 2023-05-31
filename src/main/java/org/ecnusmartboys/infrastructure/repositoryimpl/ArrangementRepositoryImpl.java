package org.ecnusmartboys.infrastructure.repositoryimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecnusmartboys.domain.model.arrangement.Arrangement;
import org.ecnusmartboys.domain.model.arrangement.ArrangementInfo;
import org.ecnusmartboys.domain.repository.ArrangementRepository;
import org.ecnusmartboys.infrastructure.convertor.ArrangementConvertor;
import org.ecnusmartboys.infrastructure.data.mysql.ArrangementDO;
import org.ecnusmartboys.infrastructure.mapper.ArrangementMapper;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ArrangementRepositoryImpl implements ArrangementRepository {

    private final ArrangementMapper arrangementMapper;

    private final ArrangementConvertor arrangementConvertor;

    @Override
    public void save(String userId, Date date) {
        ArrangementDO arrangementDO = new ArrangementDO(date, Long.valueOf(userId));
        arrangementMapper.insert(arrangementDO);
    }

    @Override
    public List<Arrangement> retrieveByDate(Date date) {
        var arrangementDOS = arrangementMapper.selectByDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        return arrangementConvertor.toArrangements(arrangementDOS);
    }

    @Override
    public void remove(String userId, Date date) {
        ArrangementDO arrangementDO = new ArrangementDO(date, Long.valueOf(userId));
        arrangementMapper.deleteById(arrangementDO);
    }

    @Override
    public List<ArrangementInfo> retrieveMonthArrangement(Integer year, Integer month) {
        return arrangementMapper.selectInfoByMonthAndDate(year, month);
    }
}