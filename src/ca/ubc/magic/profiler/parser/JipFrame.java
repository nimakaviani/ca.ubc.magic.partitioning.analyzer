/*/////////////////////////////////////////////////////////////////////

Copyright (C) 2006 TiVo Inc.  All rights reserved.

Redistribution and use in source and binary forms, with or without 
modification, are permitted provided that the following conditions are met:

+ Redistributions of source code must retain the above copyright notice, 
  this list of conditions and the following disclaimer.
+ Redistributions in binary form must reproduce the above copyright notice, 
  this list of conditions and the following disclaimer in the documentation 
  and/or other materials provided with the distribution.
+ Neither the name of TiVo Inc nor the names of its contributors may be 
  used to endorse or promote products derived from this software without 
  specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
  INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.

/////////////////////////////////////////////////////////////////////*/

package ca.ubc.magic.profiler.parser;

import java.util.List;
import java.util.ArrayList;


/**
 * Represents an invocation of a method.  It holds the
 *
 *   * the method
 *   * the number of times it was invoked.
 *   * the child frames
 *   * the total time spent in this method
 *   * the net time spent in this function alone.
 */

public class JipFrame {

    private JipFrame mParent;
    private final JipMethod mMethod;
    private final long mThreadId;
    private final long mCount;
    private long mTotalTime;
    private long mNetTime;
    private long mDataFromParent;
    private long mDataToParent;
    private long mCountFromParent;
    private long mCountToParent;
    private List<JipFrame> mvChildren = new ArrayList<JipFrame>();

    JipFrame(JipFrame parent, JipMethod method, long threadId,
             long count, long time, long dfp, long dtp, long cfp, long ctp) {
        mParent = parent;
        mMethod = method;
        mThreadId = threadId;
        mCount = count;
        mTotalTime = time;
        mDataFromParent = dfp;
        mDataToParent = dtp;
        mCountFromParent = cfp;
        mCountToParent = ctp;

        if (mParent != null) {
            mParent.mvChildren.add(this);
        }
    }

    public JipMethod getMethod() {
        return mMethod;
    }

    public long getCount() {
        return mCount;
    }

    public long getTotalTime() {
        return mTotalTime;
    }

    public long getNetTime() {
        return mNetTime;
    }
    
    public long getDataFromParent(){
    	return mDataFromParent;
    }
    
    public long getDataToParent(){
    	return mDataToParent;
    }
    
    public long getCountFromParent(){
        return mCountFromParent;
    }
    
    public long getCountToParent(){
        return mCountToParent;
    }

    public JipFrame getParentOrNull() {
        return mParent;
    }

    public List<JipFrame> getChildren() {
        return mvChildren;
    }

    /**
     * returns true iff one of this frame's ancestors
     * has the same method as this frame.
     */
    public boolean isReentrant() {
        JipFrame scan = mParent;
        while (scan != null) {
            if (scan.getMethod().equals(mMethod)) {
                return true;
            }
            scan = scan.getParentOrNull();
        }

        return false;
    }
    
    @Override
    public String toString(){
        return ((mParent != null) ? mParent.toString() + " ->"  : "") + 
                mMethod.toString();
    }

    void computeNetTime() {
        long childTime = 0;

        for (JipFrame kid: mvChildren) {
            childTime += kid.getTotalTime();
        }
	
        mNetTime = mTotalTime - childTime;
	
        if (mNetTime < 0) {
            mNetTime = 0;
        }
    }
}
