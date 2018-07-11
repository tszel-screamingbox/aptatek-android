package com.aptatek.aptatek.util;

import com.aptatek.aptatek.domain.respository.chart.ChartDTO;
import com.aptatek.aptatek.domain.respository.chart.CubeData;
import com.aptatek.aptatek.domain.respository.chart.Measure;
import com.aptatek.aptatek.view.main.adapter.ChartVM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import ix.Ix;

public class ChartUtils {

    private static final int RANGE = 10;

    private final List<ChartDTO> chartDTOList;
    private float minLevel;
    private float delta;


    @Inject
    public ChartUtils() {
        chartDTOList = new ArrayList<>();
    }

    public List<ChartVM> asChartVMList(final List<CubeData> inputList) {
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
            float bubbleY = getY(cubeData);

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
                measureList.addAll(randomMeasureList(cubeData));
            }

            chartDTOList.add(new ChartDTO(cubeData.getId(), cubeData.getDate(), measureList, bubbleY, startY, endY));
        }

        return Ix.from(chartDTOList)
                .map(ChartVM::new)
                .toList();
    }


    private List<Measure> randomMeasureList(final CubeData cubeData) {
        final Random random = new Random();
        final int chance = random.nextInt(10);
        if (chance > 5) {
            final List<Measure> measureList = new ArrayList<>();
            for (int i = 0; i < RANGE - chance; i++) {
                measureList.add(new Measure(cubeData.getMeasuredLevel() - i, cubeData.getUnit()));
            }
            return measureList;
        } else {
            return Collections.singletonList(new Measure(cubeData.getMeasuredLevel(), cubeData.getUnit()));
        }
    }


    private float getY(final CubeData cubeData) {
        if (cubeData.getMeasuredLevel() >= 0 && delta > 0) {
            return (1 - (cubeData.getMeasuredLevel() - minLevel) / delta);
        } else {
            return 0.5f;
        }
    }
}