package com.example.serbUber.util;

import com.example.serbUber.request.LocationsForRoutesRequest;
import com.example.serbUber.request.LongLatRequest;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import com.graphhopper.util.*;
import com.graphhopper.util.shapes.GHPoint;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class GraphHopperUtil {

    public static GraphHopper createGraphHopperInstance(String ghLoc) {
        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile(ghLoc);
        // specify where to store graphhopper files
        hopper.setGraphHopperLocation("target/routing-graph-cache");

        // see docs/core/profiles.md to learn more about profiles
        hopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest").setTurnCosts(false));

        // this enables speed mode for the profile we called car
        hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));

        // now this can take minutes if it imports or a few seconds for loading of course this is dependent on the area you import
        hopper.importOrLoad();
        return hopper;
    }
    public static List<ResponsePath> routing(
        GraphHopper hopper,
        LongLatRequest startLocation,
        LongLatRequest endLocation
    ) {
        // simple configuration of the request object
        GHRequest req = new GHRequest(
            startLocation.getLat(),
            startLocation.getLon(),
            endLocation.getLat(),
            endLocation.getLon()
        )
            .setProfile("car")
            .setLocale(Locale.US);

        GHResponse rsp = hopper.route(req);

        // handle errors
        if (rsp.hasErrors())
            throw new RuntimeException(rsp.getErrors().toString());

        // use the best path, see the GHResponse class for more possibilities.
        ResponsePath path = rsp.getBest();

        // points, distance in meters and time in millis of the full path
        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        long timeInMs = path.getTime();
        Translation tr = hopper.getTranslationMap().getWithFallBack(Locale.UK);
        InstructionList il = path.getInstructions();
        // iterate over all turn instructions
        for (Instruction instruction : il) {
            System.out.println("distance " + instruction.getDistance() + " for instruction: " + instruction.getTurnDescription(tr));
        }
        return rsp.getAll();


    }

    public static List<ResponsePath> bla(GraphHopper hopper, LocationsForRoutesRequest locationsForRouteRequest) {
        // define a heading (direction) at start and destination
        LongLatRequest startLocation = locationsForRouteRequest.getLocationsForRouteRequest().get(0);
        LongLatRequest endLocation = getLongLatRequest(locationsForRouteRequest.getLocationsForRouteRequest());
        GHRequest req = new GHRequest().setProfile("car").
            addPoint(new GHPoint(startLocation.getLat(), startLocation.getLon())).addPoint(new GHPoint(endLocation.getLat(), endLocation.getLon())).
            setHeadings(Arrays.asList(350d, 180d)).
            // use flexible mode (i.e. disable contraction hierarchies) to make heading and pass_through working
                putHint(Parameters.CH.DISABLE, true);
        // if you have via points you can avoid U-turns there with
        req.getHints().putObject(Parameters.Routing.PASS_THROUGH, true);
        GHResponse res = hopper.route(req);
        if (res.hasErrors())
            throw new RuntimeException(res.getErrors().toString());
//        assert res.getAll().size() == 1;
//        assert Helper.round(res.getBest().getDistance(), -2) == 800;

        // calculate alternative routes between two points (supported with and without CH)
        req = new GHRequest().setProfile("car").
            addPoint(new GHPoint(startLocation.getLat(), startLocation.getLon())).addPoint(new GHPoint(endLocation.getLat(), endLocation.getLon())).
            setAlgorithm(Parameters.Algorithms.ALT_ROUTE);
        req.getHints().putObject(Parameters.Algorithms.AltRoute.MAX_PATHS, 2);
        res = hopper.route(req);
        if (res.hasErrors())
            throw new RuntimeException(res.getErrors().toString());

        return null;
    }

    private static LongLatRequest getLongLatRequest(List<LongLatRequest> locationsForRouteRequest) {
        return locationsForRouteRequest.get(locationsForRouteRequest.size() - 1);
    }
}
