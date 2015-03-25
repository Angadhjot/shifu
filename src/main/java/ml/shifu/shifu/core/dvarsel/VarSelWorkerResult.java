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
package ml.shifu.shifu.core.dvarsel;


import ml.shifu.guagua.io.HaltBytable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 11/24/2014.
 */
public class VarSelWorkerResult extends HaltBytable {

    private List<Integer> columnIdList = new ArrayList<Integer>(0);

    public VarSelWorkerResult() {
        // default constructor, for serialization
    }

    public VarSelWorkerResult(List<Integer> columnIdList) {
        this.columnIdList = columnIdList;
    }

    @Override
    public void doWrite(DataOutput out) throws IOException {
        out.writeInt(columnIdList.size());
        for(Integer columnId: columnIdList) {
            out.writeInt(columnId);
        }
    }

    @Override
    public void doReadFields(DataInput in) throws IOException {
        Integer size = in.readInt();
        columnIdList = new ArrayList<Integer>(size);

        for(int i = 0; i < size; i++) {
            columnIdList.add(in.readInt());
        }
    }

    public List<Integer> getColumnIdList() {
        return this.columnIdList;
    }
}
