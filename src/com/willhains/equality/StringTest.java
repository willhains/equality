/*
 * Significant portions of this file were adapted from
 * http://code.google.com/p/guicebox/source/browse/#svn%2Ftrunk%2Ftest%2Forg%2Fguicebox
 * which is distributed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.willhains.equality;

import java.util.*;

/**
 * Tests {@link ComparableTest} on a proven {@link Comparable} implementation.
 * 
 * @author willhains
 */
public final class StringTest extends ComparableTest<String>
{
	private static final Random _RND = new Random();
	
	@Override
	protected String createImp() throws Exception
	{
		return Long.toString(_RND.nextLong(), 36);
	}
	
	@Override
	protected String createEquivalent(final String o)
	{
		return new String(o);
	}
	
	@Override
	protected String createGreater(final String o)
	{
		return o + "a";
	}
	
	@Override
	protected String createLesser(final String o)
	{
		return o.substring(0, o.length() - 1);
	}
}
