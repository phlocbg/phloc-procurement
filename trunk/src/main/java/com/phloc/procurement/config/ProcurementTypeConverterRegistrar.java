/**
 * Copyright (C) 2006-2013 phloc systems
 * http://www.phloc.com
 * office[at]phloc[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phloc.procurement.config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.phloc.commons.annotations.IsSPIImplementation;
import com.phloc.commons.typeconvert.ITypeConverter;
import com.phloc.commons.typeconvert.ITypeConverterRegistrarSPI;
import com.phloc.commons.typeconvert.ITypeConverterRegistry;
import com.phloc.procurement.idscheme.EntityIDScheme;
import com.phloc.procurement.idscheme.EntityIDSchemeManager;

/**
 * This is the SPI implementation of the type converter registry for this
 * project.
 * 
 * @author philip
 */
@IsSPIImplementation
public class ProcurementTypeConverterRegistrar implements ITypeConverterRegistrarSPI
{
  public void registerTypeConverter (@Nonnull final ITypeConverterRegistry aRegistry)
  {
    // implementation class
    aRegistry.registerTypeConverter (EntityIDScheme.class, String.class, new ITypeConverter ()
    {
      @Nonnull
      public String convert (@Nonnull final Object aSource)
      {
        return ((EntityIDScheme) aSource).getID ();
      }
    });
    aRegistry.registerTypeConverter (String.class, EntityIDScheme.class, new ITypeConverter ()
    {
      @Nullable
      public EntityIDScheme convert (@Nonnull final Object aSource)
      {
        return EntityIDSchemeManager.getInstance ().getIDSchemeFromID ((String) aSource);
      }
    });
  }
}
