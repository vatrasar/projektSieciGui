package lab4.La;

import lab4.Dane;
import lab4.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LaAlgorithm {
    Dane data;

    public LaAlgorithm(Dane data) {
        this.data = data;
    }

    public List<List<Sensor>>getShedule()
    {
        Environment environment=new Environment(data.getListOfPoi(),data.getListOfSensors(),data.getPromien());
        List<List<Sensor>>result=new ArrayList<>();
        for(int i=1;i<data.laData.maxRunsNumber;i++)
        {
            List<Sensor> bestSolutionForCurrentState=getBestSolutionForCurrentState(environment,data.getListOfSensors());
            if(bestSolutionForCurrentState!=null)//null mean that target coverage ratio hasn't been achieved
            {
                result.add(bestSolutionForCurrentState);
            }else {
                break;
            }
            environment.eraseBattery();

        }
        return result;
    }

    /**
     * @return null mean that target coverage ratio hasn't been achieved
     * @param environment
     * @param listOfSensors
     */
    private List<Sensor> getBestSolutionForCurrentState(Environment environment, List<Sensor> listOfSensors) {

        int C_u=0;
        environment.resetSumC();
        Random random=new Random(data.randomSeed);
        initMemory(environment);
        environment.setRandomSensorsStatesKAndReadyToShare(data.randomSeed,data.laData.probSensorOn,data.laData.maxK,data.laData.probReadyToShare);
        for(int i=0;i<data.laData.maxIterationsNumber;i++)
        {


            environment.setSensorsStatesAccordingToBestStrategyInMemory(data.laData.epslion,data.randomSeed);

            environment.discontReward(data);

        }

        if(environment.getCoverageRate()<data.getQ())
        {
            return null;
        }
        return getSolution(environment,listOfSensors);
    }

    private List<Sensor> getSolution(Environment environment, List<Sensor> sensorsList) {
        List<Sensor>solution=new ArrayList<>();
        for(int i=0;i<sensorsList.size();i++)
        {
            if(environment.sensorsList.get(i).getStan()==1)
            {
                solution.add(sensorsList.get(i));
            }
        }
        return solution;
    }

    public void initMemory(Environment environment) {
//        Environment temp_environment=new Environment(environment,data.getPromien());
        environment.setRandomSensorsStatesKAndReadyToShare(data.randomSeed,data.laData.probSensorOn,data.laData.maxK, data.laData.probReadyToShare);
        for(int i=0;i<data.laData.h;i++)
        {
            environment.setNewStateAccordingToRandomStrategy(data.randomSeed,data.laData);
            environment.discontReward(data);
        }

    }


}
