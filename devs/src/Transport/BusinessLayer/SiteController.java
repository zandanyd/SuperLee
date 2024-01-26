package SuperLee.Transport.BusinessLayer;
import SuperLee.Transport.DataLayer.SiteDAO;

import java.util.ArrayList;
import java.util.HashMap;

public class SiteController { // Singleton class

//    private HashMap<ShippingArea, ArrayList<Site>> sitesByAreaToVisit; //
//    private HashMap<String, Site> sitesByAddress; // all sites

//    private ArrayList<Site> allSites; // all sites
    private SiteDAO siteDAO;
    private static SiteController single_instance = null;

    private SiteController()  {
        siteDAO = SiteDAO.getInstance();
//        sitesByAreaToVisit = new HashMap<>();
//        sitesByAddress = new HashMap<>();
//        allSites = new ArrayList<>();
    }
    public static synchronized SiteController getInstance(){
        if(single_instance == null){
            single_instance = new SiteController();
        }
        return single_instance;
    }

    //----------------------Sites functions---------------------------------



    public boolean checkSiteExist(String address){
        return siteDAO.checkSiteExist(address); // TODO fix!
    }

    public void addSite(Site site){
        siteDAO.addSite(site);
//       allSites.add(site);
    }

    public void printSitesByArea(ShippingArea area){
        ArrayList<Site> sites = siteDAO.getSitesByArea(area);
        for (Site site : sites){
            System.out.println(site.toString());
        }
    }

    // A function that searches for a site by its address and returns null if didn't find
    public Site getSiteByAddress(String address) {
        return siteDAO.getSiteByAddress(address);
    }


    public boolean selectSourceToDocument(String address, TransportDocument transportDocument){
        Site site = getSiteByAddress(address);
        if (site == null){
            return false;
        }
        return true;
    }

    public ArrayList<Site> getSitesByArea(ShippingArea area){
        return siteDAO.getSitesByArea(area);
    }

    public ArrayList<Site> getAllSites() {
        return siteDAO.getAllSites();
    }

    public HashMap<String, String> getArrivalTimes(String sourceAddres, ArrayList<String> destinations, String departureTime)  {
        return siteDAO.getArrivalTimes(sourceAddres, destinations, departureTime);
    }

    // TODO for ido and dvir
    public void displayArrivalTimes(String sourceAddres, ArrayList<String> destinations, String departureTime){
        HashMap<String, String>hashDistance = siteDAO.getArrivalTimes(sourceAddres, destinations, departureTime);
        for (String address : hashDistance.keySet()){
            Site site = getSiteByAddress(address);
            System.out.println(site + "\n" + "Arrival transport Time : " +hashDistance.get(address).toString() + "\n");
        }
    }


    //----------------------- for DocumentGUI--------
    public ArrayList<String> filterSitesAdressByArea(ArrayList<String> adresses, ShippingArea area){
        ArrayList<String> filteredAddresses = new ArrayList<>();
        for (String address : adresses) {
            Site site = getSiteByAddress(address);
            if (site.getShippingArea().equals(area)) {
                filteredAddresses.add(address);
            }
        }
        return filteredAddresses;
    }

}
