//============================================================================
//
// Copyright (C) 2002-2004  David Schneider
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//============================================================================

package com.atlassw.tools.eclipse.checkstyle.projectconfig;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import com.atlassw.tools.eclipse.checkstyle.config.CheckConfigurationFactory;
import com.atlassw.tools.eclipse.checkstyle.config.ICheckConfiguration;
import com.atlassw.tools.eclipse.checkstyle.util.CheckstylePluginException;

/**
 * A File Set is a collection of files audited with a common set of audit rules.
 */
public class FileSet implements Cloneable
{
    //=================================================
    // Public static final variables.
    //=================================================

    //=================================================
    // Static class variables.
    //=================================================

    //=================================================
    // Instance member variables.
    //=================================================

    private String             mName;

    private String             mCheckConfigName   = "";

    private ICheckConfiguration mCheckConfig;

    private boolean            mEnabled           = true;

    private List               mFileMatchPatterns = new LinkedList();

    //=================================================
    // Constructors & finalizer.
    //=================================================

    /**
     * Default constructor.
     */
    public FileSet()
    {

    }

    /**
     * Default constructor.
     * 
     * @param name The name of the <code>FileSet</code>
     * 
     * @param checkConfig The name of the <code>CheckConfiguration</code> used
     *            to check this <code>FileSet</code>.
     */
    public FileSet(String name, ICheckConfiguration checkConfig)
    {
        setName(name);
        setCheckConfig(checkConfig);
    }

    //=================================================
    // Methods.
    //=================================================

    /**
     * Returns a list of <code>FileMatchPattern</code> objects.
     * 
     * @return List
     */
    public List getFileMatchPatterns()
    {
        return mFileMatchPatterns;
    }

    /**
     * Set the list of <code>FileMatchPattern</code> objects.
     * 
     * @param list The new list of pattern objects.
     */
    public void setFileMatchPatterns(List list)
    {
        mFileMatchPatterns = list;
    }

    /**
     * Get the check configuration used by this file set.
     * 
     * @return The check configuration used to audit files in the file set.
     */
    public ICheckConfiguration getCheckConfig()
    {
        ICheckConfiguration config = null;
        try
        {
            config = CheckConfigurationFactory.getByName(mCheckConfigName);
        }
        catch (CheckstylePluginException e)
        {
            //  Just return null.
        }

        return config;
    }

    /**
     * Sets the check configuration used by this file set.
     * 
     * @param checkConfig the check configuration
     */
    public void setCheckConfig(ICheckConfiguration checkConfig)
    {
        if (checkConfig != null)
        {
            mCheckConfigName = checkConfig.getName();
        }
        mCheckConfig = checkConfig;
    }

    /**
     * Returns the name.
     * 
     * @return String
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Sets the name.
     * 
     * @param name The name to set
     */
    public void setName(String name)
    {
        mName = name;
    }

    /**
     * Returns the enabled flag.
     * 
     * @return boolean
     */
    public boolean isEnabled()
    {
        return mEnabled;
    }

    /**
     * Sets the enabled flag.
     * 
     * @param enabled The enabled to set
     */
    public void setEnabled(boolean enabled)
    {
        mEnabled = enabled;
    }

    /**
     * {@inheritDoc}
     */
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {

        if (obj == null || !(obj instanceof FileSet))
        {
            return false;
        }
        if (this == obj)
        {
            return true;
        }
        FileSet otherFileSet = (FileSet) obj;
        if (!mName.equals(otherFileSet.mName)
                || !mCheckConfigName.equals(otherFileSet.mCheckConfigName)
                || mEnabled != otherFileSet.mEnabled)
        {
            return false;
        }
        if (!mFileMatchPatterns.equals(otherFileSet.mFileMatchPatterns))
        {
            return false;
        }

        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        //a "nice" prime number, see Java Report, April 2000
        final int prime = 1000003;

        int result = 1;
        result = (result * prime) + Boolean.valueOf(mEnabled).hashCode();
        result = (result * prime) + (mName != null ? mName.hashCode() : 0);
        result = (result * prime) + (mCheckConfigName != null ? mCheckConfigName.hashCode() : 0);
        result = (result * prime)
                + (mFileMatchPatterns != null ? mFileMatchPatterns.hashCode() : 0);

        return result;
    }

    /**
     * Tests a file to see if its included in the file set.
     * 
     * @param file The file to test.
     * 
     * @return <code>true</code>= the file is included in the file set,
     *         <p>
     *         <code>false</code>= the file is not included in the file set.
     * 
     * @throws CheckstylePluginException Error during processing.
     */
    public boolean includesFile(IFile file) throws CheckstylePluginException
    {
        boolean result = false;
        String filePath = file.getProjectRelativePath().toString();

        Iterator iter = mFileMatchPatterns.iterator();
        while (iter.hasNext())
        {
            FileMatchPattern pattern = (FileMatchPattern) iter.next();
            boolean matches = pattern.isMatch(filePath);
            if (matches)
            {
                if (pattern.isIncludePattern())
                {
                    result = true;
                }
                else
                {
                    result = false;
                }
            }
        }

        return result;
    }

    /**
     * @return The name of the check configuration used by the
     *         <code>FileSet</code>.
     */
    public String getCheckConfigName()
    {
        return mCheckConfigName;
    }

}