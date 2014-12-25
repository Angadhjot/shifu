/**
 * Copyright [2012-2014] eBay Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ml.shifu.shifu.core.processor;

import ml.shifu.shifu.container.obj.ColumnConfig;
import ml.shifu.shifu.core.validator.ModelInspector.ModelStep;
import ml.shifu.shifu.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Initialize processor, the purpose of this processor is create columnConfig based on modelConfig instance
 */
public class InitModelProcessor extends BasicModelProcessor implements Processor {

    /**
     * log object
     */
    private final static Logger log = LoggerFactory.getLogger(InitModelProcessor.class);

    /**
     * runner for init the model
     * 
     * @throws Exception
     */
    @Override
    public int run() throws Exception {
        log.info("Step Start: init");
        long start = System.currentTimeMillis();
        setUp(ModelStep.INIT);

        // initialize ColumnConfig list
        int status = initColumnConfigList();
        if(status != 0) {
            return status;
        }

        // save ColumnConfig list into file
        saveColumnConfigList();

        clearUp(ModelStep.INIT);
        log.info("Step Finished: init with {} ms", (System.currentTimeMillis() - start));
        return 0;
    }

    /**
     * initialize the columnConfig file
     * 
     * @throws IOException
     */
    private int initColumnConfigList() throws IOException {
        String[] fields = CommonUtils.getHeaders(modelConfig.getHeaderPath(), modelConfig.getHeaderDelimiter(),
                modelConfig.getDataSet().getSource());

        columnConfigList = new ArrayList<ColumnConfig>();
        for(int i = 0; i < fields.length; i++) {
            String varName = fields[i];
            ColumnConfig config = new ColumnConfig();
            config.setColumnNum(i);
            config.setColumnName(varName);
            columnConfigList.add(config);
        }

        CommonUtils.updateColumnConfigFlags(modelConfig, columnConfigList);

        boolean hasTarget = false;
        for(ColumnConfig config: columnConfigList) {
            if(config.isTarget()) {
                hasTarget = true;
            }
        }

        if(!hasTarget) {
            log.error("Target is not valid: " + modelConfig.getTargetColumnName());
            return 1;
        }

        return 0;
    }

}
