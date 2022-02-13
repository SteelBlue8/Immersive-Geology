package com.igteam.immersivegeology.client.guidebook.importer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.igteam.immersivegeology.ImmersiveGeology;
import com.igteam.immersivegeology.client.guidebook.helper.data.IGGuideBookPageData;
import com.igteam.immersivegeology.common.util.IGLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class IGGuideBookDataHolder {

    public static IGGuideBookDataHolder INSTANCE = new IGGuideBookDataHolder();

    public HashMap<Integer, IGGuideBookPageData> guide_book_data;

    private IGGuideBookDataHolder() {
        guide_book_data = new HashMap<Integer, IGGuideBookPageData>();
        try {
            readPageIndexFile();
        } catch (IOException e){
            IGLogger.error("Error Reading IG Guide Index File: " + e.getMessage());
        }
    }

    private ArrayList<String> page_index_list = new ArrayList<String>();

    private void readPageIndexFile() throws IOException {
        ResourceLocation file = new ResourceLocation(ImmersiveGeology.MODID, "guidebook/data/page_index.txt");
        IResourceManager manager = Minecraft.getInstance().getResourceManager();
        IResource resource = manager.getResource(file);
        InputStream stream = resource.getInputStream();
        InputStreamReader i_reader = new InputStreamReader(stream);
        BufferedReader b_reader = new BufferedReader(i_reader);
        Stream<String> s_stream = b_reader.lines();
        s_stream.forEach(page_index_list::add);

        page_index_list.forEach((s) -> {
            ResourceLocation page = new ResourceLocation(ImmersiveGeology.MODID, "guidebook/data/" + s + ".json");
            try {
                createPageData(page);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void createPageData(ResourceLocation file) throws IOException {
        IResourceManager manager = Minecraft.getInstance().getResourceManager();
        IResource resource = manager.getResource(file);
        InputStream stream = resource.getInputStream();
        InputStreamReader i_reader = new InputStreamReader(stream);
        BufferedReader b_reader = new BufferedReader(i_reader);
        Stream<String> s_stream = b_reader.lines();
        StringBuilder jsonString = new StringBuilder("");
        s_stream.forEach(jsonString::append);

        JsonReader reader = new JsonReader(new StringReader(jsonString.toString()));

        Gson gson = new Gson();

        IGGuideBookPageData test = gson.fromJson(reader, IGGuideBookPageData.class);

        reader.close();
        b_reader.close();
        i_reader.close();

        guide_book_data.put(test.getId(), test);
    }

}
