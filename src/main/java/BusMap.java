import java.util.*;

import static java.lang.Math.pow;

public class BusMap {
    Set<CriticalSquare> criticalSquares = new HashSet<>();
    Set<CriticalSquare> uncoveredCriticalSquares;
    List<BusRoute> busRoutes = new ArrayList<>();
    boolean isInitialized = false;
    float radius;

    public void busMapInitEA(float radius) {
        calculateIntervalsForBusRoutes(radius);
        calculateCriticalPointsCoverableSquares();
        isInitialized = true;
    }

    private void calculateIntervalsForBusRoutes(float radius) {
        for (CriticalSquare criticalSquare : this.uncoveredCriticalSquares) {
            for (int i = 0; i < busRoutes.size(); i++) {
                BusRoute busRoute = busRoutes.get(i);
                busRoute.calculateIntervals(criticalSquare, radius);
                busRoutes.set(i, busRoute);
            }
        }
    }

    private void calculateCriticalPointsCoverableSquares() {
        for (int i = 0; i < busRoutes.size(); i++) {
            BusRoute busRoute = busRoutes.get(i);
            Set<CriticalSquare> currentCoveredCriticalSquare;
            boolean isBeginningOrder = false;

            Collections.sort(busRoute.criticalPoints, (criticalPoint1, criticalPoint2) -> {
                if (criticalPoint1.polyLineOrder > criticalPoint2.polyLineOrder) {
                    return Integer.MAX_VALUE;
                } else if (criticalPoint1.polyLineOrder == criticalPoint2.polyLineOrder) {
//                    boolean isDistancePoint1GreaterThanDistancePoint2 = (pow(criticalPoint1.x - busRoute.polyPoints.get(criticalPoint1.polyLineOrder).x, 2) + pow(criticalPoint1.y - busRoute.polyPoints.get(criticalPoint1.polyLineOrder).y, 2)
//                            > pow(criticalPoint2.x - busRoute.polyPoints.get(criticalPoint2.polyLineOrder).x, 2) + pow(criticalPoint2.y - busRoute.polyPoints.get(criticalPoint1.polyLineOrder).y, 2));
                    if ((pow(criticalPoint1.x - busRoute.polyPoints.get(criticalPoint1.polyLineOrder).x, 2) + pow(criticalPoint1.y - busRoute.polyPoints.get(criticalPoint1.polyLineOrder).y, 2)
                            > pow(criticalPoint2.x - busRoute.polyPoints.get(criticalPoint2.polyLineOrder).x, 2) + pow(criticalPoint2.y - busRoute.polyPoints.get(criticalPoint1.polyLineOrder).y, 2))) {
                        return 1;
                    } else if ((pow(criticalPoint1.x - busRoute.polyPoints.get(criticalPoint1.polyLineOrder).x, 2) + pow(criticalPoint1.y - busRoute.polyPoints.get(criticalPoint1.polyLineOrder).y, 2)
                            == pow(criticalPoint2.x - busRoute.polyPoints.get(criticalPoint2.polyLineOrder).x, 2) + pow(criticalPoint2.y - busRoute.polyPoints.get(criticalPoint1.polyLineOrder).y, 2))){
                        return criticalPoint1.isBeginning ? -1 : 1;
                    } else {
                        return -1;
                    }
                } else {
                    return Integer.MIN_VALUE;
                }
            });

//            Set<CriticalSquare> coveredCriticalSquaresAtI = new HashSet<>();
//            for (int j = 0; j < busRoute.criticalPoints.size(); j++) {
//                CriticalPoint criticalPoint = busRoute.criticalPoints.get(j);
//                if (busRoute.criticalPoints.get(i).isBeginning) {
//                    coveredCriticalSquaresAtI.add(busRoute.criticalPoints.get(i).criticalSquare);
//                } else {
//                    coveredCriticalSquaresAtI.remove(busRoute.criticalPoints.get(i).criticalSquare);
//                }
////                observableCrticalSquare.add(new HashSet<>(coveredCriticalSquaresAtI));
//                criticalPoint.coverableCriticalSquares = new HashSet<>(coveredCriticalSquaresAtI);
//            }

            currentCoveredCriticalSquare = new HashSet<CriticalSquare>();
            for (int j = 0; j < busRoute.criticalPoints.size(); j++) {
                CriticalPoint criticalPoint = busRoute.criticalPoints.get(j);
                if (criticalPoint.isBeginning) {
                    currentCoveredCriticalSquare.add(criticalPoint.criticalSquare);
                    for (Iterator<CriticalSquare> itr = currentCoveredCriticalSquare.iterator(); itr.hasNext();) {
                        itr.next().criticalPointsIndex.add(new Pair<>(busRoute.routeID, j));
                    }
                    criticalPoint.coverableCriticalSquares = new HashSet<>(currentCoveredCriticalSquare);
                } else {
                    criticalPoint.coverableCriticalSquares = new HashSet<>(currentCoveredCriticalSquare);
                    for (Iterator<CriticalSquare> itr = currentCoveredCriticalSquare.iterator(); itr.hasNext();) {
                        itr.next().criticalPointsIndex.add(new Pair<>(busRoute.routeID, j));
                    }
                    currentCoveredCriticalSquare.remove(criticalPoint.criticalSquare);
                }
//                else {
//                    if (criticalPoint.isBeginning) {
//                        criticalPoint.coverableCriticalSquares = currentCoveredCriticalSquare;
////                        criticalPoint.fitness = calculateCriticalPointFitness(currentCoveredCriticalSquare);
//                        currentCoveredCriticalSquare.remove(criticalPoint.criticalSquare);
//                    } else {
//                        currentCoveredCriticalSquare.add(criticalPoint.criticalSquare);
//                        criticalPoint.coverableCriticalSquares = currentCoveredCriticalSquare;
////                        criticalPoint.fitness = calculateCriticalPointFitness(currentCoveredCriticalSquare);
//                    }
//                }
                busRoute.criticalPoints.set(j, criticalPoint);
            }
            busRoutes.set(i, busRoute);
        }
    }
}
