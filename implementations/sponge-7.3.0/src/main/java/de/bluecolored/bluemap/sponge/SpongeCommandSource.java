/*
 * This file is part of BlueMap, licensed under the MIT License (MIT).
 *
 * Copyright (c) Blue (Lukas Rieger) <https://bluecolored.de>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.bluecolored.bluemap.sponge;

import java.util.Optional;

import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Locatable;

import com.flowpowered.math.vector.Vector3d;

import de.bluecolored.bluemap.common.plugin.Plugin;
import de.bluecolored.bluemap.common.plugin.serverinterface.CommandSource;
import de.bluecolored.bluemap.common.plugin.text.Text;
import de.bluecolored.bluemap.core.world.World;

public class SpongeCommandSource implements CommandSource {

	private Plugin plugin;
	private org.spongepowered.api.command.CommandSource delegate;
	
	public SpongeCommandSource(Plugin plugin, org.spongepowered.api.command.CommandSource delegate) {
		this.plugin = plugin;
		this.delegate = delegate;
	}
	
	@Override
	public void sendMessage(Text text) {
		org.spongepowered.api.text.Text spongeText = TextSerializers.JSON.deserializeUnchecked(text.toJSONString());
		delegate.sendMessage(spongeText);
	}

	@Override
	public boolean hasPermission(String permission) {
		return delegate.hasPermission(permission);
	}
	
	@Override
	public Optional<Vector3d> getPosition() {
		if (delegate instanceof Locatable) {
			return Optional.of(((Locatable) delegate).getLocation().getPosition());
		}
		
		return Optional.empty();
	}
	
	@Override
	public Optional<World> getWorld() {
		if (delegate instanceof Locatable) {
			return Optional.ofNullable(plugin.getWorld(((Locatable) delegate).getLocation().getExtent().getUniqueId()));
		}
		
		return Optional.empty();
	}

}
