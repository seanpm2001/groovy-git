/*
 * Copyright 2003-2007, 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.groovy.control.messages;

import java.io.PrintWriter;

import org.codehaus.groovy.control.Janitor;
import org.codehaus.groovy.control.ProcessingUnit;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.SyntaxException;

/**
 *  A base class for compilation messages.
 *
 *  @author <a href="mailto:cpoirier@dreaming.org">Chris Poirier</a>
 */
public abstract class Message {
   /**
    *  Writes the message to the specified {@code PrintWriter}.  The supplied
    *  {@code ProcessingUnit} is the unit that holds this {@code Message}.
    *
    * @param writer the {@code PrintWriter } to write to.
    * @param janitor
    */
    public abstract void write(PrintWriter writer, Janitor janitor);

   /**
    *  A synonym for write(writer, owner, null).
    *
    * @param writer the {@code PrintWriter } to write to.
    */
    public final void write(PrintWriter writer) {
        write(writer,  null);
    }

  //---------------------------------------------------------------------------
  // FACTORY METHODS

   /**
    *  Creates a new {@code Message} from the specified text.
    *
    * @param text the text to wrap.
    * @param owner the {@code ProcessingUnit}.
    * @return the {@code Message}.
    */
    public static Message create(String text, ProcessingUnit owner) {
        return new SimpleMessage(text, owner);
    }

   /**
    *  Creates a new {@code Message} from the specified text.
    *
    * @param text the text to wrap.
    * @param data
    * @param owner the {@code ProcessingUnit}.
    * @return the {@code Message}.
    */
    public static Message create(String text, Object data, ProcessingUnit owner) {
        return new SimpleMessage(text, data, owner);
    }

   /**
    *  Creates a new {@code Message} from the specified {@code SyntaxException}.
    *
    * @param error
    * @param owner the {@code ProcessingUnit}.
    * @return the {@code Message}.
    */
    public static Message create(SyntaxException error, SourceUnit owner) {
        return new SyntaxErrorMessage(error, owner);
    }
}
