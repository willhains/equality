/*
 * Significant portions of this file were adapted from
 * http://code.google.com/p/guicebox/source/browse/#svn%2Ftrunk%2Ftest%2Forg%2Fguicebox
 * which is distributed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.willhains.equality;

import org.junit.*;

/**
 * Superclass for the "Abstract Test Pattern", which is a convenient way to test a class for compliance with an
 * interface's contract.
 * 
 * @param <T> the interface contract being tested.
 * @author willhains
 */
@Ignore
public abstract class AbstractTest<T>
{
	private T _imp;
	
	protected abstract T createImp() throws Exception;
	
	protected final T imp()
	{
		return _imp;
	}
	
	@Before
	public void setUp() throws Exception
	{
		_imp = createImp();
	}
}
