package br.com.tomvieira.relgenerator;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;

/**
 * REST Web Service
 *
 * @author Wellington
 */
@Path("/pdf")
public class PDFGeneratorResource {

    @Context
    private UriInfo context;
    
    @Context 
    ServletContext contextServlet; 

    //Este projeto requer phantomjs instalado no servidor com comando no path
    @GET
    @Produces("application/pdf")
    public Response getRelatorio(@QueryParam("url") String url) {
        try {
            javax.ws.rs.core.Response.ResponseBuilder responseBuilder = null;
            File finalFile = new File("/tmp/relatorio.pdf");
//            URL htmlFileUrl = new URL(url);
//            File htmlFile = new File("/tmp/htmlFile.html");
                        
//            URLConnection conn = htmlFileUrl.openConnection();
//            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
//            conn.connect();
//            FileUtils.copyInputStreamToFile(conn.getInputStream(), htmlFile);
                                    
            
            File configFile = new File(contextServlet.getRealPath("/")+"/WEB-INF/rasterize.js");

            ProcessBuilder renderProcess = new ProcessBuilder(
                    "phantomjs",
                    configFile.getAbsolutePath(),
                    url,
                    finalFile.getAbsolutePath(),
                    "A4"
            );

            Process phantom = renderProcess.start();
            int exitCode = phantom.waitFor();
            
            if (exitCode != 0) {
                return javax.ws.rs.core.Response.serverError().build();
            }
            responseBuilder = javax.ws.rs.core.Response.ok((Object) finalFile);
            responseBuilder.type("application/pdf");
            responseBuilder.header("Content-Disposition", "attachment; filename=relatorio.pdf");
            return responseBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return javax.ws.rs.core.Response.serverError().build();
        }
    }
}
