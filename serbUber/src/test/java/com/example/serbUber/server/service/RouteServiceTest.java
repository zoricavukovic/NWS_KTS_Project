package com.example.serbUber.server.service;

import com.example.serbUber.dto.PossibleRoutesViaPointsDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Location;
import com.example.serbUber.model.Route;
import com.example.serbUber.repository.RouteRepository;
import com.example.serbUber.request.DrivingLocationIndexRequest;
import com.example.serbUber.request.LocationsForRoutesRequest;
import com.example.serbUber.request.LongLatRequest;
import com.example.serbUber.service.*;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.example.serbUber.server.service.helper.LocationHelper.*;
import static com.example.serbUber.util.Constants.getBeforeLastIndexOfList;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.github.tomakehurst.wiremock.client.WireMock.get;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RouteServiceTest {
    @Mock
    private RouteRepository routeRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private VehicleTypeInfoService vehicleTypeInfoService;

    @Mock
    private DrivingLocationIndexService drivingLocationIndexService;

    @InjectMocks
    private RouteService routeService;

    @Test
    @DisplayName("T1-Should create route")
    public void shouldCreateRoute() {
        List<DrivingLocationIndexRequest> drivingLocationIndexRequests = createDrivingLocationIndexRequests();
        SortedSet<DrivingLocationIndex> drivingLocationIndexList = createDrivingLocationIndex();

        double time = 5.2;
        double distance = 4.7;
        List<Integer> routePathIndex = Arrays.asList(1, 2);
        when(locationService.tryToFindLocation(LOCATION_LON_LAT_1[0], LOCATION_LON_LAT_1[1])).thenReturn(FIRST_LOCATION);
        when(locationService.tryToFindLocation(LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1])).thenReturn(null);

        doReturn(SECOND_LOCATION).when(locationService).create(CITY, STREET_2, NUMBER_2, ZIP_CODE, LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1]);

        when(drivingLocationIndexService.create(
            FIRST_LOCATION,
            routePathIndex.get(0),
            1
        )).thenReturn(drivingLocationIndexList.first());

        when(drivingLocationIndexService.create(
            SECOND_LOCATION,
            routePathIndex.get(1),
            -1
        )).thenReturn(drivingLocationIndexList.last());

        Route expectedRoute = createTestRoute(drivingLocationIndexList, time, distance);
        when(routeRepository.save(any(Route.class))).thenReturn(expectedRoute);

        Route createdRoute = routeService.createRoute(drivingLocationIndexRequests, time, distance, routePathIndex);

        assertNotNull(createdRoute);

        assertEquals(expectedRoute.getDistance(), createdRoute.getDistance(), 0);
        assertEquals(expectedRoute.getTimeInMin(), createdRoute.getTimeInMin(), 0);
        assertEquals(expectedRoute.getLocations(), createdRoute.getLocations());

        verify(locationService, times(1)).create(CITY, STREET_2, NUMBER_2, ZIP_CODE, LOCATION_LON_LAT_2[0], LOCATION_LON_LAT_2[1]);
        verify(drivingLocationIndexService, times(drivingLocationIndexRequests.size())).create(any(Location.class), anyInt(), anyInt());
    }

    @Test
    @DisplayName("T2-Should return before last index of list")
    public void shouldGetBeforeLastIndexOfList() {

        assertEquals(-1, getBeforeLastIndexOfList(new ArrayList<>()));

        List<LongLatRequest> oneElementList = new ArrayList<>();
        oneElementList.add(new LongLatRequest(1, 2));
        assertEquals(0, getBeforeLastIndexOfList(oneElementList));

        List<LongLatRequest> moreThanOneElementList = new ArrayList<>();
        moreThanOneElementList.add(new LongLatRequest(1, 2));
        moreThanOneElementList.add(new LongLatRequest(3, 4));
        moreThanOneElementList.add(new LongLatRequest(5, 6));
        assertEquals(2, getBeforeLastIndexOfList(moreThanOneElementList));
    }

    @Test
    @DisplayName("T3-Should throw entity not found when vehicle type not found")
    public void shouldReturnEmptyListOfPossibleRoutes() throws EntityNotFoundException {
        LocationsForRoutesRequest locationsForRoutesRequest = createLocationsForRoutesRequest(2);

        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/routed-car/route/v1/driving/1,2;3,4?geometries=geojson&overview=false&alternatives=true&steps=true"))
                .willReturn(aResponse().withStatus(200).withBody("{ /* JSON response */ }")));

        when(vehicleTypeInfoService.getAveragePriceForChosenRoute(anyDouble())).thenThrow(new EntityNotFoundException("CAR", EntityType.VEHICLE_TYPE_INFO));

        List<PossibleRoutesViaPointsDTO> result = routeService.getPossibleRoutes(locationsForRoutesRequest);
        wireMockServer.stop();

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("T4-Should return possible routes list")
    public void shouldReturnListOfPossibleRoutes() throws EntityNotFoundException {
        LocationsForRoutesRequest locationsForRoutesRequest = createLocationsForRoutesRequest(2);

        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("https://routing.openstreetmap.de/routed-car/route/v1/driving/1,2;3,4?geometries=geojson&overview=false&alternatives=true&steps=true"))
                .willReturn(aResponse().withStatus(200).withBody("{code=Ok, routes=[{legs=[{steps=[{geometry={coordinates=[[45.094954, 18.970306], [45.102297, 18.970897], [45.104856, 18.971339]], type=LineString}, maneuver={bearing_after=85, bearing_before=0, location=[45.094954, 18.970306], type=depart}, mode=driving, driving_side=right, name=, intersections=[{out=0, entry=[true], bearings=[85], location=[45.094954, 18.970306]}], weight=153.5, duration=153.5, distance=1049.9}, {geometry={coordinates=[[45.104856, 18.971339], [45.105366, 18.967623], [45.105716, 18.965079], [45.108712, 18.943722], [45.108872, 18.942568], [45.109314, 18.939362], [45.109927, 18.932238], [45.109857, 18.915743], [45.109548, 18.879139], [45.109522, 18.869407], [45.109556, 18.862937], [45.109556, 18.862911], [45.109531, 18.860384], [45.109339, 18.857602], [45.108638, 18.853091], [45.107188, 18.847672], [45.097444, 18.814326], [45.093789, 18.802157], [45.087956, 18.782423], [45.087334, 18.780885], [45.08665, 18.779272], [45.085248, 18.776482], [45.083427, 18.773449], [45.082284, 18.771783], [45.080771, 18.76983], [45.077562, 18.766132], [45.05216, 18.737762], [45.050141, 18.735635], [45.048652, 18.734181], [45.046703, 18.732487], [45.044773, 18.731003], [45.042895, 18.729677], [45.041018, 18.728493], [45.036896, 18.726199], [45.012088, 18.712802], [44.987286, 18.699406], [44.984737, 18.698028], [44.982768, 18.696826], [44.981328, 18.695828], [44.979914, 18.694747], [44.978198, 18.693297], [44.976771, 18.691928], [44.973919, 18.688961], [44.948612, 18.662587], [44.945618, 18.659482], [44.942748, 18.656295], [44.941307, 18.654481], [44.939938, 18.652608], [44.937377, 18.648791], [44.921632, 18.625131], [44.918221, 18.620058]], type=LineString}, maneuver={bearing_after=171, bearing_before=78, location=[45.104856, 18.971339], modifier=right, type=turn}, mode=driving, ref=177, driving_side=right, name=, intersections=[{out=0, in=1, entry=[true, false, false], bearings=[180, 255, 345], location=[45.104856, 18.971339]}, {out=1, in=0, entry=[false, true, true], bearings=[0, 180, 270], location=[45.105366, 18.967623]}, {out=1, in=0, entry=[false, true, true], bearings=[0, 165, 270], location=[45.105716, 18.965079]}, {out=0, in=2, entry=[true, true, false], bearings=[180, 270, 345], location=[45.108872, 18.942568]}, {out=2, in=0, entry=[false, true, true], bearings=[0, 90, 180], location=[45.109548, 18.879139]}, {out=2, in=0, entry=[false, true, true], bearings=[0, 90, 180], location=[45.109556, 18.862937]}], weight=1994.199999999, duration=1994.199999999, distance=47082.3}, {geometry={coordinates=[[44.918221, 18.620058], [44.917041, 18.620133], [44.814943, 18.630213], [44.808497, 18.630816], [44.799997, 18.631642], [44.787531, 18.632862], [44.777625, 18.633827], [44.767813, 18.634784], [44.757003, 18.635827], [44.745436, 18.636962], [44.739723, 18.637517], [44.736448, 18.637833], [44.731836, 18.638286], [44.723102, 18.639138], [44.716148, 18.63981], [44.708112, 18.640597], [44.701073, 18.641277], [44.693165, 18.642044], [44.686465, 18.642698], [44.678422, 18.643477], [44.671965, 18.644108], [44.66757, 18.644536], [44.661043, 18.645155], [44.65012, 18.646238], [44.642903, 18.646931], [44.634505, 18.647756], [44.626824, 18.648489], [44.616116, 18.649532], [44.611536, 18.649972], [44.604258, 18.650672], [44.591849, 18.651872], [44.584398, 18.652586], [44.555666, 18.655358], [44.520513, 18.658743], [44.479962, 18.662656], [44.475546, 18.662936], [44.471704, 18.66279], [44.468457, 18.66238], [44.465489, 18.661787], [44.461368, 18.660474], [44.459046, 18.659531], [44.455243, 18.65767], [44.448174, 18.654045], [44.44349, 18.651692], [44.431863, 18.645769], [44.428765, 18.644248], [44.426317, 18.643304], [44.424561, 18.642808], [44.421916, 18.64223], [44.419582, 18.641935], [44.417244, 18.641838], [44.41476, 18.641894], [44.412579, 18.642144], [44.410973, 18.642465], [44.40884, 18.642924], [44.403333, 18.644837], [44.40059, 18.645512], [44.398334, 18.645877], [44.395449, 18.646071], [44.393328, 18.646027], [44.391005, 18.645833], [44.388148, 18.645307], [44.385554, 18.644598], [44.383043, 18.643617], [44.38067, 18.6425], [44.37841, 18.641107], [44.372776, 18.637283], [44.361393, 18.629587], [44.351412, 18.622803], [44.342483, 18.616737], [44.328556, 18.607304], [44.316821, 18.599327], [44.306306, 18.592194], [44.290169, 18.581278], [44.286372, 18.578719], [44.283817, 18.577289], [44.280995, 18.576133], [44.277611, 18.575163], [44.275123, 18.574693], [44.272707, 18.574445], [44.266377, 18.574287], [44.262832, 18.574171], [44.26041, 18.573938], [44.254805, 18.572872], [44.252912, 18.572392], [44.250257, 18.571379], [44.246449, 18.569384], [44.244717, 18.568443], [44.244293, 18.568072], [44.242619, 18.566856], [44.241112, 18.565478], [44.23954, 18.564034], [44.238923, 18.563434], [44.238487, 18.562993], [44.236509, 18.561263], [44.234293, 18.559259], [44.232368, 18.557469], [44.231145, 18.556452], [44.22998, 18.555577], [44.228269, 18.55425], [44.226971, 18.553451], [44.222275, 18.550732], [44.220387, 18.549837], [44.218025, 18.548611], [44.217315, 18.548053], [44.216671, 18.547398], [44.216241, 18.546654], [44.215457, 18.544954], [44.215008, 18.543874], [44.214938, 18.543524], [44.214867, 18.543041], [44.214804, 18.542645], [44.214705, 18.542068], [44.214352, 18.541546], [44.214071, 18.541354], [44.213426, 18.540946], [44.211932, 18.540155], [44.210821, 18.539563]], type=LineString}, maneuver={bearing_after=272, bearing_before=212, location=[44.918221, 18.620058], modifier=right, type=turn}, mode=driving, driving_side=right, name=, intersections=[{out=2, in=0, entry=[false, true, true], bearings=[30, 210, 270], location=[44.918221, 18.620058]}, {out=2, in=0, entry=[false, true, true], bearings=[90, 180, 270], location=[44.678422, 18.643477]}, {out=1, in=0, entry=[false, true, true], bearings=[60, 240, 300], location=[44.316821, 18.599327]}, {out=1, in=0, entry=[false, true, true], bearings=[60, 240, 315], location=[44.246449, 18.569384]}, {out=1, in=0, entry=[false, true, true], bearings=[45, 225, 345], location=[44.238487, 18.562993]}, {out=2, in=0, entry=[false, true, true], bearings=[15, 30, 210], location=[44.214705, 18.542068]}, {out=2, in=0, entry=[false, false, true], bearings=[30, 210, 240], location=[44.214352, 18.541546]}], weight=5193.3, duration=5193.3, distance=79343.3}, {geometry={coordinates=[[44.210821, 18.539563], [44.210717, 18.539511], [44.209498, 18.538875], [44.208529, 18.538396], [44.207657, 18.537997]], type=LineString}, maneuver={bearing_after=241, bearing_before=240, location=[44.210821, 18.539563], modifier=straight, type=new name}, mode=driving, driving_side=right, name=, intersections=[{out=2, in=0, entry=[false, false, true], bearings=[60, 150, 240], location=[44.210821, 18.539563]}, {out=2, in=0, entry=[false, true, true], bearings=[60, 150, 240], location=[44.210717, 18.539511]}], weight=24.6, duration=24.6, distance=376.5}, {geometry={coordinates=[[44.207657, 18.537997], [44.207615, 18.538017], [44.207528, 18.538014], [44.207488, 18.537992], [44.207459, 18.537961], [44.207442, 18.537923]], type=LineString}, maneuver={bearing_after=277, bearing_before=243, location=[44.207657, 18.537997], modifier=slight left, type=end of road}, mode=driving, driving_side=right, name=, intersections=[{out=2, in=0, entry=[false, false, true], bearings=[60, 150, 285], location=[44.207657, 18.537997]}, {out=1, in=0, entry=[false, true, true], bearings=[105, 225, 345], location=[44.207528, 18.538014]}], weight=3.2, duration=3.2, distance=28.2}, {geometry={coordinates=[[44.207442, 18.537923], [44.207439, 18.537881], [44.205442, 18.536953], [44.204848, 18.536674], [44.204773, 18.536637]], type=LineString}, maneuver={bearing_after=182, bearing_before=220, location=[44.207442, 18.537923], modifier=slight right, type=turn}, mode=driving, driving_side=right, name=, intersections=[{out=1, in=0, entry=[false, true, false], bearings=[45, 180, 330], location=[44.207442, 18.537923]}, {out=2, in=0, entry=[false, true, true], bearings=[0, 150, 240], location=[44.207439, 18.537881]}, {out=2, in=0, entry=[false, true, true], bearings=[60, 150, 240], location=[44.205442, 18.536953]}], weight=21.2, duration=21.2, distance=318}, {geometry={coordinates=[[44.204773, 18.536637], [44.204731, 18.536657], [44.204684, 18.536662], [44.204641, 18.536654], [44.204281, 18.537019], [44.204235, 18.537062], [44.203959, 18.537309], [44.203763, 18.537519], [44.203369, 18.538011], [44.202747, 18.538786], [44.201917, 18.539821], [44.201533, 18.540292], [44.201164, 18.540738], [44.200887, 18.541072], [44.200137, 18.542016], [44.199766, 18.542485], [44.198968, 18.543491], [44.198909, 18.543568], [44.198176, 18.544524], [44.197823, 18.544986], [44.196435, 18.546731], [44.195507, 18.547899], [44.194702, 18.548883], [44.194333, 18.549409], [44.193526, 18.55089], [44.193412, 18.551087], [44.193013, 18.551781], [44.192711, 18.552428], [44.192611, 18.552643], [44.192473, 18.55294], [44.191154, 18.555561], [44.190988, 18.555806], [44.190972, 18.555922], [44.189969, 18.557926], [44.188106, 18.561549], [44.185849, 18.565923], [44.179579, 18.578189], [44.179405, 18.578498]], type=LineString}, maneuver={bearing_after=277, bearing_before=243, location=[44.204773, 18.536637], modifier=right, type=turn}, mode=driving, driving_side=right, name=, intersections=[{out=2, in=0, entry=[false, false, true], bearings=[60, 165, 285], location=[44.204773, 18.536637]}, {out=2, in=0, entry=[false, true, true], bearings=[105, 225, 315], location=[44.204641, 18.536654]}, {out=2, in=0, entry=[false, true, true], bearings=[135, 225, 315], location=[44.204235, 18.537062]}, {out=2, in=0, entry=[false, true, true], bearings=[150, 240, 330], location=[44.203369, 18.538011]}, {out=2, in=0, entry=[false, true, true], bearings=[150, 225, 330], location=[44.202747, 18.538786]}, {out=2, in=0, entry=[false, true, true], bearings=[135, 225, 315], location=[44.201164, 18.540738]}, {out=2, in=0, entry=[false, true, true], bearings=[150, 240, 330], location=[44.200137, 18.542016]}, {out=2, in=0, entry=[false, true, true], bearings=[150, 240, 330], location=[44.199766, 18.542485]}, {out=2, in=1, entry=[true, false, true], bearings=[60, 150, 330], location=[44.198968, 18.543491]}, {out=2, in=0, entry=[false, true, true], bearings=[150, 225, 330], location=[44.198909, 18.543568]}, {out=2, in=0, entry=[false, true, true], bearings=[150, 225, 330], location=[44.198176, 18.544524]}, {out=2, in=0, entry=[false, true, true], bearings=[150, 240, 330], location=[44.193412, 18.551087]}, {out=2, in=0, entry=[false, true, true], bearings=[150, 240, 330], location=[44.192711, 18.552428]}, {out=2, in=1, entry=[true, false, true], bearings=[60, 150, 330], location=[44.192611, 18.552643]}, {out=2, in=0, entry=[false, true, true], bearings=[150, 255, 330], location=[44.191154, 18.555561]}, {out=2, in=1, entry=[true, false, true], bearings=[60, 150, 345], location=[44.190988, 18.555806]}], weight=356.2, duration=356.2, distance=5384.3}, {geometry={coordinates=[[44.179405, 18.578498], [44.179271, 18.578454], [44.17809, 18.578436], [44.176984, 18.57846], [44.176064, 18.578492], [44.174748, 18.578612], [44.174015, 18.578715], [44.170991, 18.579373], [44.163797, 18.580953], [44.163792, 18.580954], [44.161764, 18.581443], [44.159721, 18.581918], [44.158166, 18.582076], [44.156661, 18.582165], [44.155618, 18.582126], [44.154908, 18.582099], [44.15479, 18.582094], [44.150123, 18.581885], [44.150035, 18.581881], [44.147883, 18.581799], [44.146816, 18.581822], [44.146588, 18.581827], [44.146093, 18.581848], [44.145333, 18.581867], [44.144068, 18.581858], [44.141115, 18.581742], [44.140521, 18.581746], [44.140069, 18.58175], [44.139434, 18.581782], [44.138866, 18.581873], [44.136758, 18.58239], [44.136008, 18.582599], [44.134369, 18.582956], [44.133512, 18.583213], [44.132019, 18.583615], [44.130755, 18.583995], [44.130166, 18.584159], [44.129748, 18.584218], [44.129308, 18.58419], [44.128912, 18.584107], [44.127466, 18.583456], [44.126856, 18.583208], [44.126612, 18.58316], [44.125107, 18.58322], [44.124479, 18.583228], [44.123318, 18.583172], [44.121926, 18.583398], [44.121035, 18.583496], [44.120222, 18.583379], [44.120074, 18.583063], [44.120148, 18.582491], [44.120333, 18.58186], [44.12032, 18.581369], [44.118977, 18.579873], [44.117966, 18.578751], [44.116377, 18.577084], [44.115144, 18.575436]], type=LineString}, maneuver={bearing_after=250, bearing_before=330, location=[44.179405, 18.578498], modifier=left, type=turn}, mode=driving, driving_side=right, name=, intersections=[{out=1, in=0, entry=[false, true, true], bearings=[150, 255, 330], location=[44.179405, 18.578498]}, {out=2, in=0, entry=[false, true, true, false], bearings=[75, 150, 270, 330], location=[44.179271, 18.578454]}, {out=2, in=1, entry=[true, false, true], bearings=[0, 105, 285], location=[44.163797, 18.580953]}, {out=2, in=0, entry=[false, true, true], bearings=[90, 180, 270], location=[44.156661, 18.582165]}, {out=1, in=0, entry=[false, true, true], bearings=[90, 270, 330], location=[44.155618, 18.582126]}, {out=1, in=0, entry=[false, true, true], bearings=[90, 270, 315], location=[44.15479, 18.582094]}, {out=2, in=0, entry=[false, true, true], bearings=[90, 195, 270], location=[44.150035, 18.581881]}, {out=2, in=0, entry=[false, true, true], bearings=[90, 195, 270], location=[44.146816, 18.581822]}, {out=1, in=0, entry=[false, true, true], bearings=[90, 270, 315], location=[44.146588, 18.581827]}, {out=2, in=0, entry=[false, true, true], bearings=[90, 210, 270], location=[44.140521, 18.581746]}, {out=3, in=1, entry=[true, false, true, true], bearings=[0, 105, 210, 285], location=[44.136758, 18.58239]}, {out=2, in=0, entry=[false, true, true], bearings=[90, 150, 270], location=[44.125107, 18.58322]}, {out=2, in=1, entry=[true, false, true], bearings=[15, 105, 270], location=[44.121926, 18.583398]}, {out=1, in=0, entry=[false, true, true], bearings=[45, 225, 270], location=[44.118977, 18.579873]}], weight=1071.2, duration=1071.2, distance=7440.8}, {geometry={coordinates=[[44.115144, 18.575436], [44.115648, 18.573541], [44.115599, 18.572343], [44.115592, 18.572177], [44.115368, 18.568493], [44.115274, 18.566367], [44.115106, 18.563728], [44.114714, 18.562683], [44.114191, 18.561656], [44.111892, 18.559193], [44.11086, 18.557999], [44.109949, 18.556944], [44.108249, 18.554641], [44.107221, 18.55333], [44.106156, 18.551948], [44.105905, 18.551626]], type=LineString}, maneuver={bearing_after=164, bearing_before=215, location=[44.115144, 18.575436], modifier=left, type=turn}, mode=driving, driving_side=right, name=, intersections=[{out=1, in=0, entry=[false, true, true], bearings=[30, 165, 225], location=[44.115144, 18.575436]}, {out=1, in=0, entry=[false, true, true], bearings=[0, 180, 225], location=[44.115599, 18.572343]}, {out=2, in=0, entry=[false, true, true], bearings=[30, 135, 225], location=[44.114191, 18.561656]}, {out=1, in=0, entry=[false, true, true], bearings=[45, 225, 270], location=[44.11086, 18.557999]}, {out=2, in=0, entry=[false, true, true], bearings=[30, 150, 210], location=[44.107221, 18.55333]}], weight=427.4, duration=427.4, distance=2968}, {geometry={coordinates=[[44.105905, 18.551626], [44.104981, 18.551082], [44.103402, 18.550605], [44.101753, 18.55039], [44.100259, 18.549995], [44.098525, 18.549585], [44.098108, 18.549659], [44.097492, 18.550243], [44.096317, 18.55086], [44.094646, 18.551303], [44.092516, 18.551659], [44.091985, 18.551686], [44.090569, 18.551048], [44.089161, 18.550088], [44.08807, 18.54937], [44.086952, 18.548679], [44.086358, 18.546961], [44.085678, 18.544559], [44.084928, 18.542767], [44.084128, 18.541179], [44.083485, 18.539969], [44.082721, 18.538466], [44.082152, 18.537047], [44.08168, 18.535952], [44.081375, 18.534558], [44.081301, 18.533583], [44.081171, 18.532087], [44.081036, 18.530977], [44.080845, 18.530544], [44.080611, 18.530498], [44.080333, 18.530714], [44.080149, 18.53086], [44.080019, 18.53079], [44.079877, 18.53065], [44.079656, 18.530445], [44.079643, 18.530048], [44.079699, 18.529697], [44.079545, 18.529429], [44.079532, 18.529101], [44.079625, 18.528815], [44.079717, 18.52819], [44.079705, 18.527576], [44.079643, 18.527372], [44.079495, 18.527302], [44.079434, 18.527372], [44.079378, 18.527611], [44.079292, 18.52798], [44.079187, 18.528447], [44.079156, 18.528605], [44.079046, 18.528716], [44.078867, 18.528745], [44.07859, 18.528669], [44.078442, 18.528453], [44.078306, 18.527787], [44.078214, 18.52753], [44.077647, 18.527115], [44.076439, 18.526542], [44.075663, 18.526069], [44.075071, 18.525455], [44.074935, 18.525193], [44.074849, 18.524801], [44.074868, 18.524509], [44.074658, 18.524182], [44.074356, 18.523872], [44.074128, 18.52358], [44.074202, 18.523381], [44.07427, 18.523136], [44.074085, 18.522972], [44.073876, 18.522779], [44.07353, 18.522674], [44.07337, 18.522569], [44.073118, 18.522376], [44.072828, 18.522359], [44.072508, 18.522441], [44.072045, 18.522657], [44.071867, 18.522616], [44.071552, 18.522511], [44.071238, 18.522481], [44.070936, 18.522452], [44.070782, 18.522359], [44.070757, 18.522172], [44.070788, 18.521973], [44.071072, 18.521599], [44.071164, 18.521254], [44.071035, 18.520302], [44.071022, 18.519747], [44.071207, 18.518876], [44.071713, 18.51807], [44.071608, 18.517509], [44.071774, 18.516866], [44.072015, 18.516288], [44.072526, 18.514997], [44.072896, 18.513331], [44.07276, 18.512764], [44.072015, 18.512373], [44.071824, 18.512221], [44.071571, 18.512163], [44.07138, 18.51204], [44.071306, 18.511812], [44.07117, 18.511613], [44.070727, 18.511128], [44.069864, 18.51031], [44.069328, 18.510082], [44.069106, 18.509825], [44.068761, 18.509451], [44.06836, 18.508914], [44.068274, 18.508598], [44.067997, 18.508394], [44.06743, 18.508242], [44.066962, 18.507985], [44.06621, 18.507535], [44.065982, 18.507371], [44.065834, 18.507283], [44.06544, 18.507137], [44.06494, 18.506886], [44.064238, 18.506495], [44.063123, 18.505612], [44.061983, 18.504911], [44.061366, 18.504701], [44.060615, 18.504631], [44.059241, 18.504116], [44.058057, 18.503614], [44.05701, 18.503327], [44.055808, 18.502924], [44.054841, 18.502679], [44.053756, 18.50189], [44.052894, 18.501726], [44.052462, 18.501796], [44.051994, 18.501954], [44.051409, 18.502194], [44.050318, 18.501533], [44.049708, 18.500633], [44.04943, 18.499798], [44.049067, 18.499254], [44.048617, 18.498793], [44.04821, 18.497846], [44.048062, 18.497326], [44.047631, 18.496718], [44.047114, 18.496239], [44.046171, 18.495994], [44.045216, 18.495596], [44.043971, 18.494761], [44.042862, 18.493907], [44.042343, 18.493789], [44.041812, 18.493876], [44.041083, 18.494413], [44.040396, 18.495239], [44.038577, 18.496347], [44.0368, 18.497159], [44.035463, 18.497112], [44.033828, 18.496917], [44.032837, 18.496327], [44.031761, 18.49544], [44.031378, 18.494877], [44.031548, 18.493621], [44.031378, 18.492997], [44.031308, 18.492091], [44.0306, 18.49105], [44.029623, 18.489835], [44.028512, 18.488587], [44.027266, 18.486908], [44.026232, 18.485693], [44.025157, 18.485089], [44.024314, 18.484485], [44.023309, 18.483552], [44.022481, 18.483169], [44.021254, 18.482434], [44.020755, 18.481894], [44.019688, 18.481493], [44.01839, 18.481075], [44.017183, 18.480516], [44.015359, 18.479951], [44.014082, 18.479373]], type=LineString}, maneuver={bearing_after=237, bearing_before=216, location=[44.105905, 18.551626], modifier=slight right, type=turn}, mode=driving, driving_side=right, name=, intersections=[{out=2, in=0, entry=[false, true, true], bearings=[30, 210, 240], location=[44.105905, 18.551626]}], weight=5593.7, duration=5593.7, distance=15536.6}, {geometry={coordinates=[[44.014082, 18.479373], [44.012547, 18.479461], [44.011846, 18.479302], [44.011389, 18.479187], [44.009913, 18.479452], [44.007587, 18.479993], [44.006392, 18.480365], [44.005747, 18.480631], [44.004552, 18.480799], [44.003188, 18.48087], [44.002562, 18.481029], [44.001572, 18.480923], [44.000993, 18.481056], [44.000713, 18.481268], [44.000629, 18.481419], [44.000685, 18.48149], [44.000732, 18.481587], [44.000676, 18.481694], [44.000312, 18.481826], [43.999462, 18.482252], [43.999181, 18.482455], [43.99877, 18.482677], [43.998042, 18.483279], [43.997948, 18.483474], [43.998061, 18.483749], [43.998257, 18.483864], [43.998406, 18.483917], [43.998584, 18.483926], [43.998789, 18.483979], [43.999144, 18.48436], [43.999209, 18.484688], [43.999359, 18.484989], [43.999462, 18.485077], [44.000078, 18.485308], [44.000517, 18.4856], [44.000517, 18.486318], [44.000526, 18.48839], [44.000601, 18.489391], [44.000377, 18.490933], [44.000274, 18.493156], [44.00048, 18.495503], [44.000713, 18.496283], [44.000965, 18.498515], [44.0012, 18.499804]], type=LineString}, maneuver={bearing_after=272, bearing_before=244, location=[44.014082, 18.479373], modifier=slight right, type=turn}, mode=driving, driving_side=right, name=, intersections=[{out=2, in=0, entry=[false, true, true], bearings=[60, 240, 270], location=[44.014082, 18.479373]}], weight=1379, duration=1379, distance=3830.6}, {geometry={coordinates=[[44.0012, 18.499804], [44.0012, 18.499804]], type=LineString}, maneuver={bearing_after=0, bearing_before=10, location=[44.0012, 18.499804], modifier=left, type=arrive}, mode=driving, driving_side=right, name=, intersections=[{in=0, entry=[true], bearings=[190], location=[44.0012, 18.499804]}], weight=0, duration=0, distance=0}], summary=177, weight=16217.5, duration=16217.5, distance=163358.4}], weight_name=routability, weight=16217.5, duration=16217.5, distance=163358.4}], waypoints=[{hint=9eqKg_7qioOIAQAAAAAAAAAAAADnBQAAPPiHQwAAAAAAAAAAlz2DRIgBAAAAAAAAAAAAAOcFAAAUGwAAKhiwAsJ2IQFApa4CwOohAQAALxZ7K26W, distance=10525.748502302, name=, location=[45.094954, 18.970306]}, {hint=bZpXhm-aV4YJAgAA5AAAANUzAABKOgAAIdEQQ16GfEKbXGZFF46BRQkCAADkAAAA1TMAAEo6AAAUGwAAsGefAtxIGgEAY58CoEkaASoAnwB7K26W, distance=128.584700809, name=, location=[44.0012, 18.499804]}]}")));

        when(vehicleTypeInfoService.getAveragePriceForChosenRoute(anyDouble())).thenReturn(4.0);

        List<PossibleRoutesViaPointsDTO> result = routeService.getPossibleRoutes(locationsForRoutesRequest);
        wireMockServer.stop();

        assertEquals(1, result.size());
        assertEquals(result.get(0).getPossibleRouteDTOList().get(0).getAveragePrice(), 4.0);
    }

}
