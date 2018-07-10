package com.aptatek.aptatek.util;

import com.aptatek.aptatek.domain.respository.chart.ChartDTO;
import com.aptatek.aptatek.domain.respository.chart.CubeData;
import com.aptatek.aptatek.domain.respository.chart.Measure;
import com.aptatek.aptatek.view.main.adapter.ChartVM;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import ix.Ix;

public class ChartUtils {

    private float minLevel;
    private float delta;

    @Inject
    public ChartUtils() {
    }

    public List<ChartVM> asChartVMList(final List<CubeData> inputList) {
        final List<ChartDTO> chartDTOList = new ArrayList<>();
        final Comparator<CubeData> comp = (p1, p2) -> Integer.compare(p1.getMeasuredLevel(), p2.getMeasuredLevel());

        if (inputList == null || inputList.size() == 0) {
            return null;
        }

        float maxLevel = Ix.from(inputList)
                .filter(cubeData -> cubeData.getMeasuredLevel() >= 0)
                .max(comp)
                .first()
                .getMeasuredLevel();
        minLevel = Ix.from(inputList)
                .filter(cubeData -> cubeData.getMeasuredLevel() >= 0)
                .min(comp)
                .first()
                .getMeasuredLevel();
        delta = maxLevel - minLevel;

        for (int i = 0; i < inputList.size(); i++) {
            final CubeData cubeData = inputList.get(i);
            float startY = -1;
            float endY = -1;

            float bubbleY;
            if (cubeData.getMeasuredLevel() > 0 && delta > 0) {
                bubbleY = getY(cubeData);
            } else {
                bubbleY = 0.5f;
            }

//            final ChartDTO measuredDay = Ix.from(chartDTOList)
//                    .filter(chartDTO -> CalendarUtils.isSameDay(chartDTO.getDate(), cubeData.getDate()))
//                    .first();

            if (i > 0 && inputList.get(i - 1) != null) {
                startY = chartDTOList.get(i - 1).getEndLineYAxis();
            }

            if (inputList.size() - 1 != i) {
                float nextBubbleY;
                if (inputList.get(i + 1).getMeasuredLevel() >= 0) {
                    nextBubbleY = getY(inputList.get(i + 1));
                } else {
                    nextBubbleY = 0.5f;
                }

                endY = (bubbleY + nextBubbleY) / 2;
            }

            final List<Measure> measureList = new ArrayList<>();
            if (cubeData.getMeasuredLevel() >= 0) {
                measureList.add(new Measure(cubeData.getMeasuredLevel(), cubeData.getUnit()));
            }

            chartDTOList.add(new ChartDTO(cubeData.getId(), cubeData.getDate(), measureList, bubbleY, startY, endY));
        }

        return Ix.from(chartDTOList)
                .map(ChartVM::new)
                .toList();
    }

    private float getY(final CubeData cubeData) {
        return (1 - (cubeData.getMeasuredLevel() - minLevel) / delta);
    }

}